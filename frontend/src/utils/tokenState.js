// token 验证状态管理
let tokenValidated = false
let validationAttempted = false  // 是否已经尝试过验证（防止后端宕机时循环请求）

export function isTokenValidated() {
  return tokenValidated
}

export function setTokenValidated(value) {
  tokenValidated = value
  validationAttempted = true
}

export function isValidationAttempted() {
  return validationAttempted
}

export function resetTokenValidation() {
  tokenValidated = false
  validationAttempted = false
}
