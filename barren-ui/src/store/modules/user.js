import {getInfo, login} from '@/api/login'
import {getToken, removeToken, setToken} from '@/utils/auth'
import router, {resetRouter} from '@/router'

const user = {
  namespaced: true, // 添加命名空间，比如调用 dispatch('user/login')
  state: {
    token: getToken(),
    name: '',
    avatar: '',
    introduction: '',
    roles: [],
    userInfo: {}
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_INTRODUCTION: (state, introduction) => {
      state.introduction = introduction
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_USERINFO: (state, userInfo) => {
      state.userInfo = userInfo
    }
  },

  actions: {
    // user login
    login({commit}, userInfo) {
      const {username, password, code, codeKey} = userInfo;
      return new Promise((resolve, reject) => {
        login(username.trim(), password, code, codeKey).then(response => {
          const data = response.data;
          commit('SET_TOKEN', data.access_token);
          setToken(data.access_token);
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // get user info
    getInfo({commit, state}) {
      return new Promise((resolve, reject) => {
        getInfo().then(response => {
          const {data} = response;
          console.log(response);
          if (!data) {
            reject('Verification failed, please Login again.')
          }

          let {roles, name, avatar, introduction} = data;
          // 如果没有任何权限，则赋予一个默认的权限，避免请求死循环
          if (!roles || roles.length <= 0) {
            roles = ['ROLE_SYSTEM_DEFAULT']
          }
          // roles must be a non-empty array
          // if (!roles || roles.length <= 0) {
          //   reject('getInfo: roles must be a non-null array!')
          // }

          commit('SET_ROLES', roles);
          commit('SET_NAME', name);
          commit('SET_AVATAR', avatar);
          commit('SET_INTRODUCTION', introduction);
          commit('SET_USERINFO', data);
          resolve(data)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // user logout
    logout({commit, state, dispatch}) {
      return new Promise((resolve, reject) => {
        // logout(state.token).then(() => {
        //   commit('SET_TOKEN', '');
        //   commit('SET_ROLES', []);
        //   removeToken();
        //   resetRouter();
        //
        //   // reset visited views and cached views
        //   // to fixed https://github.com/PanJiaChen/vue-element-admin/issues/2485
        //   dispatch('tagsView/delAllViews', null, { root: true });
        //
        //   resolve()
        // }).catch(error => {
        //   reject(error)
        // })

        commit('SET_TOKEN', '');
        commit('SET_ROLES', []);
        removeToken();
        resetRouter();
        // reset visited views and cached views
        // to fixed https://github.com/PanJiaChen/vue-element-admin/issues/2485
        dispatch('tagsView/delAllViews', null, {root: true});
        resolve()
      })
    },

    // remove token
    resetToken({commit}) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '');
        commit('SET_ROLES', []);
        removeToken();
        resolve()
      })
    },

    // dynamically modify permissions
    async changeRoles({commit, dispatch}, role) {
      const token = role + '-token';

      commit('SET_TOKEN', token);
      setToken(token);

      const {roles} = await dispatch('getInfo');

      resetRouter();

      // generate accessible routes map based on roles
      const accessRoutes = await dispatch('permission/generateRoutes', roles, {root: true});
      // dynamically add accessible routes
      router.addRoutes(accessRoutes);

      // reset visited views and cached views
      dispatch('tagsView/delAllViews', null, {root: true})
    }
  }
};
export default user
