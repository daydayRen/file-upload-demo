import axios from "axios";
import qs from 'qs';
let server = axios.create({
  baseURL:'http://localhost:8081',
  //timeout:5000,
  withCredentials: true, // 允许携带凭据（如cookie）进行跨域请求
});
//server.defaults.headers['Content-Type'] = 'multipart/form-data';
/*server.defaults.transformRequest = (data, headers) => {
  const contentType = headers['Content-Type'];
  if (contentType === 'application/x-www-form-urlencoded') return qs.stringify(data);
  return data;
}*/

server.interceptors.response.use(res => {
  return res;
}, error => {
  return Promise.reject(error);
});

// 封装各种请求方法
export const get = (url, params) => server.get(url, { params });
export const post = (url, data) => server.post(url, data);
export const put = (url, data) => server.put(url, data);
export const deleteReq = (url, data) => server.delete(url, { data });







export default server;