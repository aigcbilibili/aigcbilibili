// persist的保存类型
// import { PersistedStateOptions } from 'pinia-plugin-persistedstate'
import { parse, stringify } from 'zipson' // NOTE 压缩加密，每个浏览器对localStorage的存储有大小限制：1/6M

/**
 * @description pinia持久化参数配置
 * @param {String} key 存储到持久化的 name
 * @param {Array} paths 需要持久化的 state name
 * @param {String} storageLocation 存放的位置
 * @return persist
 * */
const piniaPersistConfig = (key, paths, storageLocation='localStorage') => {
	let storage_ = window.localStorage
	let serializer_ = {
		deserialize: parse,
		serialize: stringify
	}
	switch(storageLocation) {
		case 'cookieStorage': {
			storage_ = document.cookie
			serializer_ = {}
			break
		} 
		case 'sessionStorage': {
			storage_ = window.sessionStorage
			break
		}
	}
	const persist = {
		key,
		storage: storage_ ,
		paths,
		serializer: serializer_
	}
	return persist
}

export default piniaPersistConfig