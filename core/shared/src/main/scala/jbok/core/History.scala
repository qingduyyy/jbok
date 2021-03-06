package jbok.core

import cats.data.OptionT
import cats.effect.Sync
import cats.implicits._
import jbok.core.genesis.{AllocAccount, GenesisConfig}
import jbok.core.models._
import jbok.core.store._
import jbok.core.sync.SyncState
import jbok.crypto.authds.mpt.{MPTrie, Node}
import jbok.evm.WorldStateProxy
import jbok.persistent.{KeyValueDB, SnapshotKeyValueStore}
import scodec.bits._

abstract class History[F[_]](val db: KeyValueDB[F])(implicit F: Sync[F]) {
  protected val log = org.log4s.getLogger

  def loadGenesis(genesis: Option[Block] = None): F[Unit] = {
    log.info(s"loading genesis data")
    for {
      headerOpt <- getBlockHeaderByNumber(0)
      _ <- headerOpt match {
        case Some(h) if h.hash == GenesisConfig.header.hash =>
          log.info("genesis data already in the database")
          F.unit

        case Some(h) =>
          F.raiseError(
            new RuntimeException("Genesis data present in the database does not match genesis block from file")
          )

        case None =>
          genesis match {
            case Some(block) => save(block, Nil, block.header.difficulty, saveAsBestBlock = true)
            case None        => loadGenesisConfig()
          }
      }
    } yield ()
  }

  def loadGenesisConfig(config: GenesisConfig = GenesisConfig.default): F[Unit]

  /**
    * Allows to query a blockHeader by block hash
    *
    * @param hash of the block that's being searched
    * @return [[BlockHeader]] if found
    */
  def getBlockHeaderByHash(hash: ByteVector): F[Option[BlockHeader]]

  def getBlockHeaderByNumber(number: BigInt): F[Option[BlockHeader]] = {
    val p = for {
      hash   <- OptionT(getHashByBlockNumber(number))
      header <- OptionT(getBlockHeaderByHash(hash))
    } yield header
    p.value
  }

  /**
    * Allows to query a blockBody by block hash
    *
    * @param hash of the block that's being searched
    * @return [[BlockBody]] if found
    */
  def getBlockBodyByHash(hash: ByteVector): F[Option[BlockBody]]

  /**
    * Allows to query for a block based on it's hash
    *
    * @param hash of the block that's being searched
    * @return Block if found
    */
  def getBlockByHash(hash: ByteVector): F[Option[Block]] = {
    val p = for {
      header <- OptionT(getBlockHeaderByHash(hash))
      body   <- OptionT(getBlockBodyByHash(hash))
    } yield Block(header, body)

    p.value
  }

  /**
    * Allows to query for a block based on it's number
    *
    * @param number Block number
    * @return Block if it exists
    */
  def getBlockByNumber(number: BigInt): F[Option[Block]] = {
    val p = for {
      hash  <- OptionT(getHashByBlockNumber(number))
      block <- OptionT(getBlockByHash(hash))
    } yield block
    p.value
  }

  /**
    * Get an account for an address and a block number
    *
    * @param address address of the account
    * @param blockNumber the block that determines the state of the account
    */
  def getAccount(address: Address, blockNumber: BigInt): F[Option[Account]]

  /**
    * Get account storage at given position
    *
    * @param rootHash storage root hash
    * @param position storage position
    */
  def getAccountStorageAt(rootHash: ByteVector, position: BigInt): F[ByteVector]

  /**
    * Returns the receipts based on a block hash
    * @param blockhash
    * @return Receipts if found
    */
  def getReceiptsByHash(blockhash: ByteVector): F[Option[List[Receipt]]]

  /**
    * Returns EVM code searched by it's hash
    * @param hash Code Hash
    * @return EVM code if found
    */
  def getEvmCodeByHash(hash: ByteVector): F[Option[ByteVector]]

  /**
    * Returns MPT node searched by it's hash
    * @param hash Node Hash
    * @return MPT node
    */
  def getMptNodeByHash(hash: ByteVector): F[Option[Node]]

  def getTotalDifficultyByHash(blockHash: ByteVector): F[Option[BigInt]]

  def getTotalDifficultyByNumber(blockNumber: BigInt): F[Option[BigInt]] = {
    val p = for {
      hash <- OptionT(getHashByBlockNumber(blockNumber))
      td   <- OptionT(getTotalDifficultyByHash(hash))
    } yield td

    p.value
  }

  def getTransactionLocation(txHash: ByteVector): F[Option[TransactionLocation]]

  def getBestBlockNumber: F[BigInt]

  def getEstimatedHighestBlock: F[BigInt]

  def getSyncStartingBlock: F[BigInt]

  def setBestBlockNumber(bestBlockNumber: BigInt): F[Unit]

  def getBestBlock: F[Block]

  /**
    * Persists full block along with receipts and total difficulty
    * @param saveAsBestBlock - whether to save the block's number as current best block
    */
  def save(block: Block, receipts: List[Receipt], totalDifficulty: BigInt, saveAsBestBlock: Boolean): F[Unit] =
    save(block) *>
      save(block.header.hash, receipts) *>
      save(block.header.hash, totalDifficulty) *>
      (if (saveAsBestBlock) {
         saveBestBlockNumber(block.header.number)
       } else {
         F.unit
       }) *>
      pruneState(block.header.number)

  /**
    * Persists a block in the underlying Blockchain Database
    *
    * @param block Block to be saved
    */
  def save(block: Block): F[Unit] =
    save(block.header) *> save(block.header.hash, block.body)

  def removeBlock(hash: ByteVector, saveParentAsBestBlock: Boolean): F[Unit]

  /**
    * Persists a block header in the underlying Blockchain Database
    *
    * @param blockHeader Block to be saved
    */
  def save(blockHeader: BlockHeader): F[Unit]

  def save(blockHash: ByteVector, blockBody: BlockBody): F[Unit]

  def save(blockHash: ByteVector, receipts: List[Receipt]): F[Unit]

  def save(hash: ByteVector, evmCode: ByteVector): F[Unit]

  def save(blockHash: ByteVector, totalDifficulty: BigInt): F[Unit]

  def saveBestBlockNumber(number: BigInt): F[Unit]

  def saveNode(nodeHash: ByteVector, nodeEncoded: ByteVector, blockNumber: BigInt): F[Unit]

  def saveAccount(address: Address, account: Account): F[Unit]

  /**
    * Returns a block hash given a block number
    *
    * @param number Number of the searchead block
    * @return Block hash if found
    */
  def getHashByBlockNumber(number: BigInt): F[Option[ByteVector]]

  def genesisHeader: F[BlockHeader] = getBlockHeaderByNumber(0).map(_.get)

  def genesisBlock: F[Block] = getBlockByNumber(0).map(_.get)

  def getWorldStateProxy(
      blockNumber: BigInt,
      accountStartNonce: UInt256,
      stateRootHash: Option[ByteVector] = None,
      noEmptyAccounts: Boolean = false
  ): F[WorldStateProxy[F]]

  def pruneState(blockNumber: BigInt): F[Unit]

  def rollbackStateChangesMadeByBlock(blockNumber: BigInt): F[Unit]

  def getSyncState: F[Option[SyncState]]

  def putSyncState(syncState: SyncState): F[Unit]

  def purgeSyncState: F[Unit]
}

object History {
  def withGenesisConfig[F[_]: Sync](db: KeyValueDB[F], genesisConfig: GenesisConfig): F[History[F]] =
    for {
      history <- apply[F](db)
      _          <- history.loadGenesisConfig(genesisConfig)
    } yield history

  def apply[F[_]: Sync](db: KeyValueDB[F]): F[History[F]] = {
    val headerStore          = new BlockHeaderStore[F](db)
    val bodyStore            = new BlockBodyStore[F](db)
    val receiptStore         = new ReceiptStore[F](db)
    val numberHashStore      = new BlockNumberHashStore[F](db)
    val txLocationStore      = new TransactionLocationStore[F](db)
    val totalDifficultyStore = new TotalDifficultyStore[F](db)
    val appStateStore        = new AppStateStore[F](db)
    val evmCodeStore         = new EvmCodeStore[F](db)
    val fastSyncStore        = new FastSyncStore[F](db)

    AddressAccountStore[F](db).map(
      accountStore =>
        new HistoryImpl[F](
          db,
          headerStore,
          bodyStore,
          receiptStore,
          numberHashStore,
          txLocationStore,
          totalDifficultyStore,
          fastSyncStore,
          appStateStore,
          accountStore,
          evmCodeStore
      ))
  }
}

class HistoryImpl[F[_]](
    db: KeyValueDB[F],
    headerStore: BlockHeaderStore[F],
    bodyStore: BlockBodyStore[F],
    receiptStore: ReceiptStore[F],
    numberHashStore: BlockNumberHashStore[F],
    txLocationStore: TransactionLocationStore[F],
    totalDifficultyStore: TotalDifficultyStore[F],
    fastSyncStore: FastSyncStore[F],
    appStateStore: AppStateStore[F],
    accountStore: AddressAccountStore[F],
    evmCodeStore: EvmCodeStore[F]
)(implicit F: Sync[F]) extends History[F](db) {

  override def loadGenesisConfig(config: GenesisConfig): F[Unit] = {
    log.info(s"load genesis config with ${config.alloc.size} alloc")
    val header = GenesisConfig.genesisHeader(config)
    val body   = GenesisConfig.genesisBody(config)
    for {
      _ <- config.alloc.toList.traverse {
        case (address, AllocAccount(balance)) =>
          accountStore.put(Address(ByteVector.fromValidHex(address)), Account(0, UInt256(balance)))
      }
      stateRootHash <- accountStore.getRootHash
      block = Block(header.copy(stateRoot = stateRootHash), body)
      _ <- save(block, Nil, block.header.difficulty, saveAsBestBlock = true)
    } yield ()
  }

  /**
    * Allows to query a blockHeader by block hash
    *
    * @param hash of the block that's being searched
    * @return [[BlockHeader]] if found
    */
  override def getBlockHeaderByHash(hash: ByteVector): F[Option[BlockHeader]] =
    headerStore.getOpt(hash)

  /**
    * Allows to query a blockBody by block hash
    *
    * @param hash of the block that's being searched
    * @return [[BlockBody]] if found
    */
  override def getBlockBodyByHash(hash: ByteVector): F[Option[BlockBody]] =
    bodyStore.getOpt(hash)

  /**
    * Get account storage at given position
    *
    * @param rootHash storage root hash
    * @param position storage position
    */
  override def getAccountStorageAt(rootHash: ByteVector, position: BigInt): F[ByteVector] = ???

  /**
    * Get an account for an address and a block number
    *
    * @param address     address of the account
    * @param blockNumber the block that determines the state of the account
    */
  override def getAccount(address: Address, blockNumber: BigInt): F[Option[Account]] =
    accountStore.getOpt(address)

  /**
    * Returns the receipts based on a block hash
    *
    * @param blockhash
    * @return Receipts if found
    */
  override def getReceiptsByHash(blockhash: ByteVector): F[Option[List[Receipt]]] =
    receiptStore.getOpt(blockhash)

  /**
    * Returns EVM code searched by it's hash
    *
    * @param hash Code Hash
    * @return EVM code if found
    */
  override def getEvmCodeByHash(hash: ByteVector): F[Option[ByteVector]] =
    evmCodeStore.getOpt(hash)

  override def getTotalDifficultyByHash(blockHash: ByteVector): F[Option[BigInt]] =
    totalDifficultyStore.getOpt(blockHash)

  override def getTransactionLocation(txHash: ByteVector): F[Option[TransactionLocation]] =
    txLocationStore.getOpt(txHash)

  override def getBestBlockNumber: F[BigInt] =
    appStateStore.getBestBlockNumber

  override def getEstimatedHighestBlock: F[BigInt] =
    appStateStore.getEstimatedHighestBlock

  override def getSyncStartingBlock: F[BigInt] =
    appStateStore.getSyncStartingBlock

  override def setBestBlockNumber(bestBlockNumber: BigInt): F[Unit] =
    appStateStore.putBestBlockNumber(bestBlockNumber)

  override def getBestBlock: F[Block] =
    getBestBlockNumber.flatMap(bn => getBlockByNumber(bn).map(_.get))

  override def removeBlock(blockHash: ByteVector, saveParentAsBestBlock: Boolean): F[Unit] = {
    val maybeBlockHeader = getBlockHeaderByHash(blockHash)
    val maybeTxList      = getBlockBodyByHash(blockHash).map(_.map(_.transactionList))

    headerStore.del(blockHash) *>
      bodyStore.del(blockHash) *>
//    totalDifficultyStorage.remove(blockHash)
      receiptStore.del(blockHash) *>
      maybeTxList.map {
        case Some(txs) => txs.map(tx => txLocationStore.del(tx.hash)).sequence.void
        case None      => F.unit
      } *>
      maybeBlockHeader.map {
        case Some(bh) =>
          val rollback = rollbackStateChangesMadeByBlock(bh.number)
          val removeMapping = getHashByBlockNumber(bh.number).flatMap {
            case Some(hash) => numberHashStore.del(bh.number)
            case None       => F.unit
          }

          val updateBest =
            if (saveParentAsBestBlock) appStateStore.putBestBlockNumber(bh.number - 1)
            else F.unit

          rollback *> removeMapping *> updateBest

        case None => F.unit
      }
  }

  /**
    * Persists a block header in the underlying Blockchain Database
    *
    * @param blockHeader Block to be saved
    */
  override def save(blockHeader: BlockHeader): F[Unit] = {
    val hash = blockHeader.hash
    headerStore.put(hash, blockHeader) *> numberHashStore.put(blockHeader.number, hash)
  }

  override def save(blockHash: ByteVector, blockBody: BlockBody): F[Unit] =
    bodyStore.put(blockHash, blockBody) *>
      blockBody.transactionList.zipWithIndex
        .map {
          case (tx, index) =>
            txLocationStore.put(tx.hash, TransactionLocation(blockHash, index))
        }
        .sequence
        .void

  override def save(blockHash: ByteVector, receipts: List[Receipt]): F[Unit] =
    receiptStore.put(blockHash, receipts)

  override def save(hash: ByteVector, evmCode: ByteVector): F[Unit] =
    evmCodeStore.put(hash, evmCode)

  override def save(blockhash: ByteVector, totalDifficulty: BigInt): F[Unit] =
    totalDifficultyStore.put(blockhash, totalDifficulty)

  override def saveAccount(address: Address, account: Account): F[Unit] =
    accountStore.put(address, account)

  override def saveBestBlockNumber(number: BigInt): F[Unit] =
    appStateStore.putBestBlockNumber(number)

  override def saveNode(nodeHash: ByteVector, nodeEncoded: ByteVector, blockNumber: BigInt): F[Unit] =
    accountStore.putNode(nodeHash, nodeEncoded)

  /**
    * Returns a block hash given a block number
    *
    * @param number Number of the searched block
    * @return Block hash if found
    */
  override def getHashByBlockNumber(number: BigInt): F[Option[ByteVector]] =
    numberHashStore.getOpt(number)

  override def pruneState(blockNumber: BigInt): F[Unit] = F.unit

  override def rollbackStateChangesMadeByBlock(blockNumber: BigInt): F[Unit] = ???

  /**
    * Returns MPT node searched by it's hash
    *
    * @param hash Node Hash
    * @return MPT node
    */
  override def getMptNodeByHash(hash: ByteVector): F[Option[Node]] = accountStore.getNodeByHash(hash)

  override def getWorldStateProxy(
      blockNumber: BigInt,
      accountStartNonce: UInt256,
      stateRootHash: Option[ByteVector],
      noEmptyAccounts: Boolean
  ): F[WorldStateProxy[F]] =
    for {
      accountStore <- AddressAccountStore[F](db, stateRootHash)
      accountProxy = SnapshotKeyValueStore(accountStore)
    } yield
      WorldStateProxy[F](
        db,
        accountProxy,
        evmCodeStore,
        stateRootHash.getOrElse(MPTrie.emptyRootHash),
        Set.empty,
        Map.empty,
        Map.empty,
        accountStartNonce,
        noEmptyAccounts
      )

  override def getSyncState: F[Option[SyncState]] = fastSyncStore.getSyncState

  override def putSyncState(syncState: SyncState): F[Unit] = fastSyncStore.putSyncState(syncState)

  override def purgeSyncState: F[Unit] = fastSyncStore.purge
}
