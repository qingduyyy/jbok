package jbok.core.validators

import jbok.core.BlockChainFixture
import jbok.core.models.{Block, BlockBody, BlockHeader}
import jbok.core.validators.OmmersInvalid._
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.prop.PropertyChecks
import scodec.bits._

class OmmersValidatorFixture extends BlockChainFixture {
  val ommer1 = BlockHeader(
    parentHash = hex"fd07e36cfaf327801e5696134b36678f6a89fb1e8f017f2411a29d0ae810ab8b",
    ommersHash = hex"7766c4251396a6833ccbe4be86fbda3a200dccbe6a15d80ae3de5378b1540e04",
    beneficiary = hex"1b7047b4338acf65be94c1a3e8c5c9338ad7d67c",
    stateRoot = hex"52ce0ff43d7df2cf39f8cb8832f94d2280ebe856d84d8feb7b2281d3c5cfb990",
    transactionsRoot = hex"56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421",
    receiptsRoot = hex"56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421",
    logsBloom =
      hex"00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
    difficulty = BigInt("17864037202"),
    number = 94,
    gasLimit = 5000,
    gasUsed = 0,
    unixTimestamp = 1438270431,
    extraData = hex"426974636f696e2069732054484520426c6f636b636861696e2e",
    mixHash = hex"c6d695926546d3d679199303a6d1fc983fe3f09f44396619a24c4271830a7b95",
    nonce = hex"62bc3dca012c1b27"
  )
  val ommer2 = BlockHeader(
    parentHash = hex"fd07e36cfaf327801e5696134b36678f6a89fb1e8f017f2411a29d0ae810ab8b",
    ommersHash = hex"7766c4251396a6833ccbe4be86fbda3a200dccbe6a15d80ae3de5378b1540e04",
    beneficiary = hex"28921e4e2c9d84f4c0f0c0ceb991f45751a0fe93",
    stateRoot = hex"e766f9c51536e9038849e5eb0a143c3b3409b5385098359837cbf3324ad22328",
    transactionsRoot = hex"56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421",
    receiptsRoot = hex"56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421",
    logsBloom =
      hex"00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
    difficulty = BigInt("17864037202"),
    number = 94,
    gasLimit = 5000,
    gasUsed = 0,
    unixTimestamp = 1438270431,
    extraData = hex"476574682f76312e302e302f6c696e75782f676f312e342e32",
    mixHash = hex"8c1ed8037984be0fe9065f8f8663c3baeeb6436868ac6915dd3c2cd5fd46fa96",
    nonce = hex"40b0b2c0b6d14706"
  )
  val ommers: List[BlockHeader] = List[BlockHeader](ommer1, ommer2)
  val ommersBlockNumber = 97

  val block89 = Block(
    BlockHeader(
      parentHash = hex"ba39b4ee19e5db0f5a85c344aa2bd2e7b0cdb2404b7d5c0e6cdc08c83f85083e",
      ommersHash = hex"1dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d49347",
      beneficiary = hex"1b7047b4338acf65be94c1a3e8c5c9338ad7d67c",
      stateRoot = hex"17e5e23d006df68fd13c0f3ce71a5e510abeee28b88a4e207fce171fbc3ef60d",
      transactionsRoot = hex"56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421",
      receiptsRoot = hex"56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421",
      logsBloom =
        hex"00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
      difficulty = BigInt("17820487647"),
      number = 89,
      gasLimit = 5000,
      gasUsed = 0,
      unixTimestamp = 1438270418,
      extraData = hex"426974636f696e2069732054484520426c6f636b636861696e2e",
      mixHash = hex"d7c06ea893693857f10675f02502ce63d74fe80b3bce9749b507888f3acd0b5d",
      nonce = hex"1d48377931a68d12"
    ),
    BlockBody(List.empty, List.empty)
  )
  val block90 = Block(
    BlockHeader(
      parentHash = hex"6da5970538eba5db93162e219182fca7e093cfe4fbd8dd0b82789adb25dcbb42",
      ommersHash = hex"1dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d49347",
      beneficiary = hex"d7e30ae310c1d1800f5b641baa7af95b2e1fd98c",
      stateRoot = hex"da6f0baf5f17d201b3d711299091ecee68cf56469b1e09704713934de1c74517",
      transactionsRoot = hex"56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421",
      receiptsRoot = hex"56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421",
      logsBloom =
        hex"00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
      difficulty = BigInt("17829189056"),
      number = 90,
      gasLimit = 5000,
      gasUsed = 0,
      unixTimestamp = 1438270422,
      extraData = hex"476574682f6b6c6f737572652f76312e302e302d66633739643332642f6c696e",
      mixHash = hex"f28f1b53323dc8a6a4fe73495e71e81947366b68d8a217daa4e349b0c939401f",
      nonce = hex"f91fd0cc60d6948c"
    ),
    BlockBody(List.empty, List.empty)
  )
  val block91 = Block(
    BlockHeader(
      parentHash = hex"69d2798993659c0d864d6f2824440b091368c147efc6c33410ef181036fc2bf1",
      ommersHash = hex"1dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d49347",
      beneficiary = hex"28921e4e2c9d84f4c0f0c0ceb991f45751a0fe93",
      stateRoot = hex"0d528286094ba4781aed7a19a9c890e07651be9b73e999585c926810ec888198",
      transactionsRoot = hex"56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421",
      receiptsRoot = hex"56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421",
      logsBloom =
        hex"00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
      difficulty = BigInt("17837894714"),
      number = 91,
      gasLimit = 5000,
      gasUsed = 0,
      unixTimestamp = 1438270425,
      extraData = hex"476574682f76312e302e302f6c696e75782f676f312e342e32",
      mixHash = hex"eadd3fbbb336d073a33e4fb9faa97be8b0e904aeb8b65eeae243e1e35d86e6c3",
      nonce = hex"20fdc1504ec955a0"
    ),
    BlockBody(List.empty, List.empty)
  )
  val block92 = Block(
    BlockHeader(
      parentHash = hex"d691ed6cf02375620d9cca9052bcf38a4a23f5c77058581c8bb54a06e2eb6ed9",
      ommersHash = hex"1dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d49347",
      beneficiary = hex"bb7b8287f3f0a933474a79eae42cbca977791171",
      stateRoot = hex"a7be51c65294e0f504e781ce19ae192998fb919805cc0822d5142500204b6917",
      transactionsRoot = hex"56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421",
      receiptsRoot = hex"56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421",
      logsBloom =
        hex"00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
      difficulty = BigInt("17846604623"),
      number = 92,
      gasLimit = 5000,
      gasUsed = 0,
      unixTimestamp = 1438270427,
      extraData = hex"476574682f4c5649562f76312e302e302f6c696e75782f676f312e342e32",
      mixHash = hex"afd3d088f65607ad7404837db220add9cf54ca0f4fb107e3f6bee9d0aca18e7f",
      nonce = hex"dd7e335a44c7e9c9"
    ),
    BlockBody(List.empty, List.empty)
  )
  val block93 = Block(
    BlockHeader(
      parentHash = hex"c86dcbd8ba3cd836bd9c00d9dababe81ed5a42310ade70a77700d42b5ff8b64c",
      ommersHash = hex"445ccaa7ee03cf387e4835482288f6b08dc351eef4ecc94b3ed8de56afd298a6",
      beneficiary = hex"bb7b8287f3f0a933474a79eae42cbca977791171",
      stateRoot = hex"38f5e900f8dee62e0fdd52818dbedfe0869fa95a876357dd94669f56f25e980b",
      transactionsRoot = hex"56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421",
      receiptsRoot = hex"56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421",
      logsBloom =
        hex"00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
      difficulty = BigInt("17855318785"),
      number = 93,
      gasLimit = 5000,
      gasUsed = 0,
      unixTimestamp = 1438270430,
      extraData = hex"476574682f4c5649562f76312e302e302f6c696e75782f676f312e342e32",
      mixHash = hex"631f88fd52a9a7ee3cc3a08945eb2ab8f4da37d7cf96592dac9af514f28365bc",
      nonce = hex"cdc4e60cbd67b791"
    ),
    BlockBody(
      List.empty,
      List[BlockHeader](
        BlockHeader(
          parentHash = hex"69d2798993659c0d864d6f2824440b091368c147efc6c33410ef181036fc2bf1",
          ommersHash = hex"1dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d49347",
          beneficiary = hex"bb7b8287f3f0a933474a79eae42cbca977791171",
          stateRoot = hex"e0ae53ea50eb6fb40b764b748ccd6d4f6184a6f3c474899c74df102310c37d6a",
          transactionsRoot = hex"56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421",
          receiptsRoot = hex"56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421",
          logsBloom =
            hex"00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
          difficulty = BigInt("17837894714"),
          number = 91,
          gasLimit = 5000,
          gasUsed = 0,
          unixTimestamp = 1438270425,
          extraData = hex"476574682f4c5649562f76312e302e302f6c696e75782f676f312e342e32",
          mixHash = hex"7e0b76b9b1698947617c1ea7cb7c36f47aefc2c4095c6df90aa6e2b3da6e49ac",
          nonce = hex"edcbf5efad298bb3"
        )
      )
    )
  )
  val block94 = Block(
    BlockHeader(
      parentHash = hex"fd07e36cfaf327801e5696134b36678f6a89fb1e8f017f2411a29d0ae810ab8b",
      ommersHash = hex"7766c4251396a6833ccbe4be86fbda3a200dccbe6a15d80ae3de5378b1540e04",
      beneficiary = hex"d7e30ae310c1d1800f5b641baa7af95b2e1fd98c",
      stateRoot = hex"fcd09860439502fcd9e3f208ed043bfeffc985b3a289f69e79f9b81f32f9010e",
      transactionsRoot = hex"56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421",
      receiptsRoot = hex"56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421",
      logsBloom =
        hex"00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
      difficulty = BigInt("17864037202"),
      number = 94,
      gasLimit = 5000,
      gasUsed = 0,
      unixTimestamp = 1438270431,
      extraData = hex"476574682f6b6c6f737572652f76312e302e302d66633739643332642f6c696e",
      mixHash = hex"33fe497dae796c62f261d10304786b0c63cd59030a0f96c811a88e90e7d02b0f",
      nonce = hex"36da15d93277d947"
    ),
    BlockBody(
      List.empty,
      List[BlockHeader](
        BlockHeader(
          parentHash = hex"6da5970538eba5db93162e219182fca7e093cfe4fbd8dd0b82789adb25dcbb42",
          ommersHash = hex"1dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d49347",
          beneficiary = hex"bb7b8287f3f0a933474a79eae42cbca977791171",
          stateRoot = hex"57a99a5a9d93104df18d3cdba5d1c84e1e5c22527c0c375e59e38944311bfe14",
          transactionsRoot = hex"56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421",
          receiptsRoot = hex"56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421",
          logsBloom =
            hex"00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
          difficulty = BigInt("17829189056"),
          number = 90,
          gasLimit = 5000,
          gasUsed = 0,
          unixTimestamp = 1438270421,
          extraData = hex"476574682f4c5649562f76312e302e302f6c696e75782f676f312e342e32",
          mixHash = hex"0688a1217172b2f81b168a25459a2cad5cc2337aab1d17b30c7d803c565bf0b3",
          nonce = hex"efc94c53e5ad946a"
        )
      )
    )
  )
  val block95 = Block(
    BlockHeader(
      parentHash = hex"665586bdd5aefc860f8ff2d186617544d5aa6e5ae7203bf54b26f310be372e91",
      ommersHash = hex"1dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d49347",
      beneficiary = hex"bb7b8287f3f0a933474a79eae42cbca977791171",
      stateRoot = hex"1b39e9e77df97fd50ad637eea384839ba6aec610015450518936c9fd8e20efed",
      transactionsRoot = hex"56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421",
      receiptsRoot = hex"56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421",
      logsBloom =
        hex"00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
      difficulty = BigInt("17872759876"),
      number = 95,
      gasLimit = 5000,
      gasUsed = 0,
      unixTimestamp = 1438270433,
      extraData = hex"476574682f4c5649562f76312e302e302f6c696e75782f676f312e342e32",
      mixHash = hex"d1ed067b52da47010ab970117677233d9da738b22fe955899f9ed2e4360fc924",
      nonce = hex"75d5ff831690242a"
    ),
    BlockBody(List.empty, List.empty)
  )
  val block96 = Block(
    BlockHeader(
      parentHash = hex"c0f772db658b2279a736f232e75d98629a53d36086e34a18f9fe65a4650d50a7",
      ommersHash = hex"1dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d49347",
      beneficiary = hex"1b7047b4338acf65be94c1a3e8c5c9338ad7d67c",
      stateRoot = hex"563401469b5c16712b251e6042620838dcf4b88b6c59969a86faa94c7b4402d8",
      transactionsRoot = hex"56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421",
      receiptsRoot = hex"56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421",
      logsBloom =
        hex"00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
      difficulty = BigInt("17881486809"),
      number = 96,
      gasLimit = 5000,
      gasUsed = 0,
      unixTimestamp = 1438270435,
      extraData = hex"426974636f696e2069732054484520426c6f636b636861696e2e",
      mixHash = hex"b4f571ecf4dcebe75260f4929a01de8b2c19c161bea20dda91bfb92298f7262f",
      nonce = hex"1dbc948cb756c2b9"
    ),
    BlockBody(List.empty, List.empty)
  )

  val ommersBlockParentHash = block96.header.hash

  blockChain.save(block89).unsafeRunSync()
  blockChain.save(block90).unsafeRunSync()
  blockChain.save(block91).unsafeRunSync()
  blockChain.save(block92).unsafeRunSync()
  blockChain.save(block93).unsafeRunSync()
  blockChain.save(block94).unsafeRunSync()
  blockChain.save(block95).unsafeRunSync()
  blockChain.save(block96).unsafeRunSync()

  val ommersValidator = new OmmersValidator(blockChain, blockChainConfig, daoForkConfig)
}

class OmmersValidatorSpec extends FlatSpec with Matchers with PropertyChecks {
  it should "validate correctly a valid list of ommers" in new OmmersValidatorFixture {
    ommersValidator.validate(ommersBlockParentHash, ommersBlockNumber, ommers).value.unsafeRunSync() shouldBe Right(())
  }

  it should "report a invalid if the list of ommers is too big" in new OmmersValidatorFixture {
    ommersValidator
      .validate(ommersBlockParentHash, ommersBlockNumber, List(ommer1, ommer2, ommer2))
      .value
      .unsafeRunSync() shouldBe Left(OmmersLengthInvalid)
  }

  it should "report a invalid if there is an invalid header in the list of ommers" in new OmmersValidatorFixture {
    val invalidOmmer1: BlockHeader = ommer1.copy(number = ommer1.number + 1)
    ommersValidator
      .validate(ommersBlockParentHash, ommersBlockNumber, List(invalidOmmer1, ommer2))
      .value
      .unsafeRunSync() shouldBe Left(OmmersNotValid)
  }

  it should "report a invalid if there is an ommer that was previously used" in new OmmersValidatorFixture {
    ommersValidator
      .validate(ommersBlockParentHash, ommersBlockNumber, List(block93.body.uncleNodesList.head, ommer2))
      .value
      .unsafeRunSync() shouldBe Left(OmmersAncestorsInvalid)
  }

  it should "report a invalid if there is an ommer which is of the last ancestors" in new OmmersValidatorFixture {
    ommersValidator
      .validate(ommersBlockParentHash, ommersBlockNumber, List(ommer1, block92.header))
      .value
      .unsafeRunSync() shouldBe Left(OmmersAncestorsInvalid)
  }

  it should "report a invalid if there is an ommer too old" in new OmmersValidatorFixture {
    ommersValidator
      .validate(ommersBlockParentHash, ommersBlockNumber, List(ommer1, block90.header))
      .value
      .unsafeRunSync() shouldBe Left(OmmersAncestorsInvalid)
  }

  it should "report a invalid if there is a duplicated ommer in the ommer list" in new OmmersValidatorFixture {
    ommersValidator
      .validate(ommersBlockParentHash, ommersBlockNumber, List(ommer1, ommer1))
      .value
      .unsafeRunSync() shouldBe Left(OmmersDuplicated)
  }

}
