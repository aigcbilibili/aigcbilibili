// 基于当前时间和用户名生成的随机数
export const generateRandomNumber = (userId) => {
    // 获取当前时间戳，并转换为字符串
    const timestamp = Date.now().toString();
    // 将当前时间戳和用户id连接起来作为种子
    const seed = timestamp + userId.toString();
    // 使用SHA-256哈希算法生成种子的哈希值
    const hash = CryptoJS.SHA256(seed).toString();
    // 将哈希值的一部分作为随机数
    const randomPart = hash.substring(0, 8);
    // 将16进制的随机数转换为10进制
    const randomNumber = parseInt(randomPart, 16);
    return randomNumber;
} 