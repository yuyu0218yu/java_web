// token 验证状态管理
let tokenValidated = false

export function isTokenValidated() {
  return tokenValidated
}

export function setTokenValidated(value) {
  tokenValidated = value
}

export function resetTokenValidation() {
  tokenValidated = false
}
