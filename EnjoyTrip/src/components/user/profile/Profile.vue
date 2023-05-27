<template>
  <b-container>
    <div style="margin-top: 5%; margin-bottom: 5%">
      <b-card-group deck style="display: grid; grid-template-columns: 3fr 5fr">
        <b-card align="center">
          <template #header>
            <h3 style="margin-top: 10px">{{ user.userName }}</h3>
          </template>
          <b-card-text>
            <!-- <b-avatar variant="light" size="200px" style="margin-top: 30px; margin-bottom: 30px"></b-avatar> -->
            <div>
              <img :src="profileimage.preview" />
              <div class="image-box">
                <input type="file" id="file" ref="file" @change="imageAddUpload" />
              </div>
            </div>
          </b-card-text>
          <template #footer>
            <b-button style="margin-top: 8%" @click="profileImageSet">사진 적용</b-button>
          </template>
        </b-card>

        <!-- 사용자 정보 -->
        <b-card style="padding-left: 10%; padding-right: 10%">
          <b-card-text style="margin-top: 3%; margin-bottom: 3%">
            <!-- 이름 -->
            <b-form-group label="이름" label-for="name-input">
              <b-form-input id="name-input" v-model="user.userName" :disabled="true"
                style="text-align: center"></b-form-input>
            </b-form-group>

            <!-- 아이디 -->
            <b-form-group label="아이디" label-for="id-input">
              <b-form-input id="id-input" v-model="user.userId" :disabled="true"
                style="text-align: center"></b-form-input>
            </b-form-group>

            <!-- 이메일 -->
            <b-form-group label="이메일" label-for="email-input">
              <b-row>
                <b-col cols="6">
                  <b-form-input id="email-id-input" v-model="user.emailId" :disabled="modify"
                    style="text-align: center"></b-form-input>
                </b-col>
                <b-col cols="1">@</b-col>
                <b-col cols="5">
                  <b-form-select
                    prepend="@"
                    id="email-domain-input"
                    :options="options"
                    v-model="user.emailDomain"
                    :disabled="modify"
                  ></b-form-select>
                </b-col>
              </b-row>
            </b-form-group>

            <!-- 상태메세지 -->
            <b-form-group label="상태메세지" label-for="email-input">
              <b-form-input id="state-input" v-model="user.status" :disabled="modify"
                style="text-align: center"></b-form-input>
            </b-form-group>

            <!-- 수정 버튼 -->
            <b-button style="margin-top: 8%" @click="moveModifyProfile">{{ modifybtn }}</b-button>
          </b-card-text>
          <!-- <template #footer>
            <small class="text-muted">Last updated 3 mins ago</small>
          </template> -->
        </b-card>
      </b-card-group>
    </div>
  </b-container>
</template>

<script>
import { mapState, mapActions } from "vuex";
import http from '@/util/http-common';

const memberStore = "memberStore";
export default {
  // name: "Profile",
  data() {
    return {
      modify: true,
      modifybtn: "수정",
      options: [
        { text: "선택하세요", value: null },
        { text: "naver.com", value: "naver.com" },
        { text: "gmail.com", value: "gmail.com" },
        { text: "kakao.com", value: "kakao.com" },
        { text: "ssafy.com", value: "ssafy.com" },
      ],
      profileimage: {},
    };
  },
  methods: {
    ...mapActions(memberStore, ["getUserInfo"]),
    moveModifyProfile() {
      if (this.modify) {
        this.modify = false;
        this.modifybtn = "적용";
      } else {
        this.modify = true;
        this.modifybtn = "수정";
        http.post(`/vuser/modify`, {
          userId: this.user.userId,
          emailId: this.user.emailId,
          emailDomain: this.user.emailDomain,
          status: this.user.status,
        }).then(({ data }) => {
          if (data == "success") {
            let msg = "등록 처리시 문제가 발생했습니다.";
            if (data === "success") {
                msg = "등록이 완료되었습니다.";
                let token = sessionStorage.getItem("access-token");
                this.getUserInfo(token);
            }
            alert(msg);
          }
        })
      }
    },
    imageAddUpload() {
      this.profileimage = {
        file: this.$refs.file.files[0],
        preview: URL.createObjectURL(this.$refs.file.files[0]),
      }
    },
    profileImageSet() {
      if (this.$refs.file.files[0]!=null) {
        var frm = new FormData();
        frm.append("files", this.$refs.file.files[0]);
        frm.append("userId", this.user.userId);
        http
          .post(`/vuser/image`, frm, {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          })
          .then(({ data }) => {
            let msg = "등록 처리시 문제가 발생했습니다.";
            if (data === "success") {
              msg = "등록이 완료되었습니다.";
              let token = sessionStorage.getItem("access-token");
              this.getUserInfo(token);
            }
            alert(msg);
          });
      }
    },
  },
  computed: {
    ...mapState(memberStore, ["userInfo", "fileInfo"]),
    user() {
      return this.userInfo;
    },
    userEmail() {
      let userEmail = this.userInfo.emailId + "@" + this.userInfo.emailDomain;
      return userEmail;
    },
  },
  created() {
    if (this.fileInfo != null) {
      http.post(`/vuser/showImage`, {
        sfolder: this.fileInfo.saveFolder,
        sfile: this.fileInfo.saveFile,
        ofile: this.fileInfo.originalFile
      }, {responseType: 'blob'})
        .then((response) => {
            const blob = new Blob([response.data]);
            const fileObjectUrl = window
                .URL
                .createObjectURL(blob);
          this.profileimage = {
            preview: fileObjectUrl,
          }
          console.log(fileObjectUrl);
      });
    }
  }
};
</script>

<style scoped>
input:disabled {
  background-color: white;
}
</style>
