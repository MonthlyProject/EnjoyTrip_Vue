<template>
  <b-modal
    id="login-modal"
    ref="login"
    title="로그인"
    @show="resetModal"
    @hidden="resetModal"
    @ok="confirm"
  >
    <findpwd ref="findpwd"></findpwd>
    <form ref="form" @submit.stop.prevent="handleSubmit">
      <!-- 아이디 -->
      <b-form-group
        label="아이디"
        label-for="user-id-input"
        :invalid-feedback="uidWarn"
        :state="userIdState"
      >
        <b-form-input
          id="user-id-input"
          v-model="userId"
          :state="userIdState"
          required
          @keyup.enter="confirm"
        ></b-form-input>
      </b-form-group>

      <!-- 비밀번호 -->
      <b-form-group
        label="비밀번호"
        label-for="user-password-input"
        :invalid-feedback="pwdWarn"
        :state="passwordState"
      >
        <b-form-input
          id="user-password-input"
          type="password"
          v-model="password"
          :state="passwordState"
          required
          @keyup.enter="confirm"
        ></b-form-input>
        <a style="color:darkgrey; cursor: pointer;" @click="FindPwd">비밀번호 찾기</a>
      </b-form-group>
    </form>
  </b-modal>
</template>

<script>
import http from "@/util/http-common.js";
import { mapState, mapActions } from "vuex";
import Findpwd from "@/components/user/modal/findpwd.vue";

const memberStore = "memberStore";

export default {
  name: "LoginModalVue",
  components: {
    Findpwd,
  },
  data() {
    return {
      userId: "",
      userIdState: false,
      password: "",
      passwordState: false,
    };
  },
  computed: {
    uidWarn: {
      set: function () {
        // return '아이디는 필수 입력입니다'
      },
      get: function () {
        if (this.userIdState === false) {
          return "아이디를 입력해주세요.";
        }
        return null;
      },
    },
    pwdWarn: {
      set: function () {
        // return '비밀번호는 필수 입력입니다'
      },
      get: function () {
        if (this.passwordState === false) {
          return "비밀번호를 입력해주세요";
        }
        return null;
      },
    },
    ...mapState(memberStore, ["isLogin", "isLoginError", "userInfo"]),
  },
  methods: {
    ...mapActions(memberStore, ["userConfirm", "getUserInfo"]),
    async confirm() {
      let user = { userId: this.userId, userPwd: this.password };
      console.log(user);
      await this.userConfirm(user);
      let token = sessionStorage.getItem("access-token");
      // console.log("1. confirm() token >> " + token);
      if (this.isLogin) {
        await this.getUserInfo(token);
        // console.log("4. confirm() userInfo :: ", this.userInfo);
        // this.$router.push({ name: "board" });
      }
    },
    FindPwd() {
      this.$refs.findpwd.$refs.findpwd.show();
    },
    movePage() {
      this.$router.push({ name: "join" });
    },
    checkFormValidity() {
      if (this.userId == "") {
        this.userIdState = false;
      } else {
        this.userIdState = undefined;
      }
      if (this.password == "") {
        this.passwordState = false;
      } else {
        this.passwordState = undefined;
      }

      return this.userIdState === undefined && this.passwordState === undefined;
    },
    resetModal() {
      this.userId = "";
      this.userIdState = undefined;
      this.password = "";
      this.passwordState = undefined;
    },
    submit(bvModalEvent) {
      // Prevent modal from closing
      bvModalEvent.preventDefault();
      // Trigger submit handler
      this.handleSubmit();
    },
    handleSubmit() {
      // 로그인시 아이디, 비밀번호 입력 안했을 때
      if (!this.checkFormValidity()) {
        //console.log(this.checkFormValidity());
        console.log("uid: " + this.userIdState);
        console.log("pwd: " + this.passwordState);
        return;
      }

      // 정상 입력했을 때
      http
        .post(`/vuser/login`, {
          userId: this.userId,
          userPwd: this.password,
        })
        .then(({ data }) => {
          let msg = "아이디 혹은 비밀번호를 다시 확인해 주세요";
          // console.log(data.message);
          if (data.message === "success") {
            msg = "로그인 완료";
            alert(msg);
            // 모달 닫기
            this.$bvModal.hide("login-modal");
            console.log("bbbb");
          } else if (data.message === "fail") {
            alert(msg);
            this.userIdState = undefined;
            this.passwordState = undefined;
            this.resetModal();
            console.log("cccc");
          }
        });
    },
  },
};
</script>

<style scoped></style>
