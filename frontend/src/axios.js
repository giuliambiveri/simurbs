import axios from 'axios';
import { app } from './main'


axios.defaults.baseURL = `${process.env.VUE_APP_BASE_URL}`;

// Add a request interceptor
axios.interceptors.request.use(config => {
    // Show loader
    app.isLoading = true;

    // Set withCredentials to true for all requests
    config.withCredentials = true;

    return config;
}, error => {
    // Do something with request error
    return Promise.reject(error);
});

// Add a response interceptor
axios.interceptors.response.use(response => {
    // Hide loader
    app.isLoading = false;

    return response;
}, error => {
    // Hide loader in case of error
    app.isLoading = false;

    // Do something with response error
    return Promise.reject(error);
});

export default axios;

