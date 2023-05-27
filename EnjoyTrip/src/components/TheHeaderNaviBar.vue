<template>
  <div>
    <b-navbar toggleable="lg" type="dark" class="background">
      <span class="brand" @click="goHome"
        >Trip Scanner<a class="reddot" style="text-decoration: none">.</a></span
      >

      <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

      <b-collapse id="nav-collapse" is-nav>
        <!-- Right aligned nav items -->
        <b-navbar-nav class="ml-auto">
          <div v-if="!userInfo">
            <a class="nav-right" @click="login">로그인</a>
            <a class="nav-right" @click="register">회원가입</a>
          </div>
          <div v-if="userInfo">
            <img v-if="profileimage" :src="profileimage" style="width: 40px; height: 40px" />
            <b-avatar v-else variant="secondary" size="2em"></b-avatar>
            <a style="margin-left: 9px">{{ userInfo.userName }}님</a>
            <a v-if="userInfo.userId != 'admin'" class="nav-right" @click="myPage">마이페이지</a>
            <a v-else class="nav-right" @click="manageuser">회원 관리</a>
            <a class="nav-right" @click="logout">로그아웃</a>
          </div>
        </b-navbar-nav>
      </b-collapse>
    </b-navbar>
  </div>
</template>

<script>
import { mapState, mapGetters, mapActions } from "vuex";
import http from "@/util/http-common";

const memberStore = "memberStore";
export default {
  data() {
    return {
      profileimage: null,
    };
  },
  methods: {
    goHome() {
      this.$router.push({ name: "home" }).catch(() => {});
    },
    register() {
      this.$emit("openRegister");
    },
    login() {
      this.$emit("openLogin");
    },
    myPage() {
      this.$router.push({ name: "profile" }).catch(() => {});
    },
    ...mapActions(memberStore, ["userLogout"]),
    logout() {
      // this.flag=false;
      this.userLogout(this.userInfo.userId);
      sessionStorage.removeItem("access-token"); //저장된 토큰 없애기
      sessionStorage.removeItem("refresh-token"); //저장된 토큰 없애기
      // this.$store.memberStore.state.isLogin = false;
      this.$router.push({ name: "home" }).catch(() => {});
    },
    manageuser() {
      this.$router.push({ name: "manage" }).catch(() => {});
    },
  },
  computed: {
    ...mapState(memberStore, ["isLogin", "userInfo", "fileInfo"]),
    ...mapGetters(["checkUserInfo"]),
  },
  watch: {
    fileInfo() {
      if (this.fileInfo != null) {
        http
          .post(
            `/vuser/showImage`,
            {
              sfolder: this.fileInfo.saveFolder,
              sfile: this.fileInfo.saveFile,
              ofile: this.fileInfo.originalFile,
            },
            { responseType: "blob" }
          )
          .then((response) => {
            const blob = new Blob([response.data]);
            const fileObjectUrl = window.URL.createObjectURL(blob);
            this.profileimage = fileObjectUrl;
          });
      } else {
        this.profileimage = null;
      }
    },
  },
  created() {
    if (this.fileInfo != null) {
      http
        .post(
          `/vuser/showImage`,
          {
            sfolder: this.fileInfo.saveFolder,
            sfile: this.fileInfo.saveFile,
            ofile: this.fileInfo.originalFile,
          },
          { responseType: "blob" }
        )
        .then((response) => {
          const blob = new Blob([response.data]);
          const fileObjectUrl = window.URL.createObjectURL(blob);
          this.profileimage = fileObjectUrl;
        });
    }
  },
};
</script>

<style scoped>
.background {
  box-shadow: 5px 5px 5px 5px rgba(0, 0, 0, 0.068);
  padding-right: 10%;
  padding-left: 10%;
}

.brand {
  font-size: 35px;
  font-weight: 500;
  padding: 5px;
  color: white;
  text-decoration: none;
  cursor: pointer;
}

.reddot {
  font-size: 30px;
  color: red;
}

.nav-right {
  color: rgba(255, 255, 255, 0.575);
  padding: 10px;
  text-decoration: none;
  cursor: pointer;
}

.nav-right:hover {
  color: rgba(255, 255, 255, 0.808);
}
</style>
