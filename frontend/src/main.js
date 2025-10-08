import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import PrimeVue from 'primevue/config';
import Lara from './presets/lara';
import 'tailwindcss/tailwind.css'
import './assets/tailwind.css'
import 'primeicons/primeicons.css';
import ConfirmationService from "primevue/confirmationservice";
import ToastService from 'primevue/toastservice';


let vue_app = createApp(App);

export const app = vue_app.use(store)
    .use(router)
    .use(PrimeVue, {unstyled: true, pt: Lara})
    .use(ConfirmationService)
    .use(ToastService)
    .mount("#app");