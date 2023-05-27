<template>
  <div id="app">
    <the-header-navi-bar-vue
      @openRegister="openRegister"
      @openLogin="openLogin"
    ></the-header-navi-bar-vue>
    <register-modal-vue ref="registerModal"></register-modal-vue>
    <login-modal-vue ref="loginModal"></login-modal-vue>
    <router-view />
    <the-footer-navi-bar></the-footer-navi-bar>
  </div>
</template>

<script>
import TheHeaderNaviBarVue from "./components/TheHeaderNaviBar.vue";
import TheFooterNaviBar from "./components/TheFooterNaviBar.vue";
import RegisterModalVue from "./components/user/modal/register.vue";
import LoginModalVue from "./components/user/modal/login.vue";

export default {
  components: {
    TheHeaderNaviBarVue,
    TheFooterNaviBar,
    RegisterModalVue,
    LoginModalVue,
  },
  methods: {
    openRegister() {
      this.$refs.registerModal.$refs.register.show();
    },
    openLogin() {
      this.$refs.loginModal.$refs.login.show();
    },
  },
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}

.background {
  background-color: #008374;
}
</style>
