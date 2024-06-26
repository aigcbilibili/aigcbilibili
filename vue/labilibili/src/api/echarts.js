
import request from "./index.js";




export const getData = async () => {
    let res = await request.get('/viewCount/getViewCount');
    return res;
}

export const addData = (data) => {
    localStorage.setItem('viewCount', JSON.stringify(data));
    request.post('/viewCount/addViewCount', data);
}
