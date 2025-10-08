import { createStore } from 'vuex';
import axios from "../axios";

export default createStore({
    state: {
        simulation: {},
        isCompareMode : false,
        editingOldSimulation : false,
        isOldCityInfoBoxOpen: false,
        isOldBlockInfoBoxOpen: false,
        isCityInfoBoxOpen: false,
        isBlockInfoBoxOpen: false,
    },
    getters: {},
    mutations: {
        setNewUsers(state, newUsers) {
            state.users = newUsers;
        }
    },
    actions: {
        async fetchUsers(context) {
            try {
                const response = await axios.get("/users");
                context.commit("setNewUsers", response.data);
            } catch (error) {
                console.error('Error fetching users:', error);
                throw new Error("Failed to fetch users");
            }
        },
        async postUser(context, userToAdd) {
            try {
                await axios.post("/users", userToAdd);
                await context.dispatch("fetchUsers");
            } catch (error) {
                console.error('Error posting user:', error);
                throw new Error("Failed to post user");
            }
        }
    },
    modules: {}
});
