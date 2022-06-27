import JSEncrypt from 'jsencrypt/bin/jsencrypt.min'

// 密钥对生成 http://web.chacuo.net/netrsakeypair

const publicKey = 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAOVVqwshYXPO7gLvO0w4etpr6dEUgyj9' +
  'W+8Fc6eMq6Sr+Fqu0AzT1Y1EGBqkLCTwEF0JLPiNokKSRBsLTUiJ/0sCAwEAAQ=='

const privateKey = 'MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEA5VWrCyFhc87uAu87' +
  'TDh62mvp0RSDKP1b7wVzp4yrpKv4Wq7QDNPVjUQYGqQsJPAQXQks+I2iQpJEGwtN' +
  'SIn/SwIDAQABAkACoakEx61GlulEP1p+1WcTeomETKf0oHOdmoJpOcP+b7UqNRVp' +
  'sQf4NmmVecXbrEsCi6+I81LmhBC3C+7dYeaBAiEA9U9Z90BhEeEy3S+WRr/21bPc' +
  'PLY7uQmMqzw5sWs4CL8CIQDvVBkPrwVDP66Td4dQegdLZYjTcl43kz8M6tp9dZIA' +
  'dQIhAMpmO2uWscCn12jKD6q2TnnKWce58BLe/zJ1kYCCX4xPAiB+7fVUshhycCXg' +
  'pDYp4m5X+IO20SNTCk8YRL6YZ3CFuQIgTZFTmrLpBDtzjS3ex8MCj6I9zYCkD5cH' +
  'i0uW0Oa7iUY='

// 加密
export function encrypt(txt) {
  const encryptor = new JSEncrypt()
  encryptor.setPublicKey(publicKey) // 设置公钥
  return encryptor.encrypt(txt) // 对数据进行加密
}

// 解密
export function decrypt(txt) {
  const encryptor = new JSEncrypt()
  encryptor.setPrivateKey(privateKey) // 设置私钥
  return encryptor.decrypt(txt) // 对数据进行解密
}

