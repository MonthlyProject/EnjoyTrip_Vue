<template>
  <div class="main-container mt-3 mb-3">
    <b-row class="mb-1">
      <b-col style="text-align: left">
        <b-form @submit="onSubmit" @reset="onReset">
          <b-form-group id="userid-group" label="작성자:" label-for="userId">
            <b-form-input
              id="userId"
              :disabled="isUserid"
              v-model="userInfo.userId"
              type="text"
              required
              readonly
              placeholder="작성자 입력..."
            ></b-form-input>
          </b-form-group>

          <b-form-group
            id="subject-group"
            label="제목:"
            label-for="subject"
            description="제목을 입력하세요."
          >
            <b-form-input
              id="subject"
              v-model="article.subject"
              type="text"
              required
              placeholder="제목 입력..."
            ></b-form-input>
          </b-form-group>

          <b-form-group id="content-group" label="내용:" label-for="content">
            <b-form-textarea
              id="content"
              v-model="article.content"
              placeholder="내용 입력..."
              rows="10"
              max-rows="15"
            ></b-form-textarea>
          </b-form-group>

          <input type="file" ref="inputFile" class="mt-3" multiple />

          <b-button type="submit" variant="primary" class="m-1">글등록</b-button>
          <b-button type="reset" variant="danger" class="m-1">초기화</b-button>
          <b-button variant="outline-primary" @click="moveList">목록</b-button>
        </b-form>
      </b-col>
    </b-row>
  </div>
</template>

<script>
import http from "@/util/http-common.js";
import { mapState } from "vuex";

const memberStore = "memberStore";

export default {
  name: "BoardModify",
  components: {},
  data() {
    return {
      article: {
        userId: "",
        subject: "",
        content: "",
        files: null,
      },
      isUserid: false,
    };
  },
  // created() {
  //     http.get(`/board/${this.$route.params.articleNo}`).then(({ data }) => {
  //         this.article = data;
  //     });
  //     this.isUserid = true;
  // },
  methods: {
    moveList() {
      this.$router.push({ name: "boardlist" });
    },
    onSubmit(event) {
      event.preventDefault();

      let err = true;
      let msg = "";
      err &&
        !this.article.subject &&
        ((msg = "제목 입력해주세요"), (err = false), this.$refs.subject.focus());
      err &&
        !this.article.content &&
        ((msg = "내용 입력해주세요"), (err = false), this.$refs.content.focus());

      if (!err) alert(msg);
      else this.registArticle();
    },
    onReset(event) {
      event.preventDefault();
      this.article.articleNo = 0;
      this.article.subject = "";
      this.article.content = "";
      // this.moveList();
    },

    registArticle() {
      this.article.files = this.$refs.inputFile.files;
      this.article.userId = this.userInfo.userId;
      console.log(this.article.files);
      var frm = new FormData();
      frm.append("userId", this.article.userId);
      frm.append("subject", this.article.subject);
      frm.append("content", this.article.content);
      if (this.article.files.length) {
        for (let i = 0; i < this.article.files.length; i++) {
          frm.append("files", this.article.files[i]);
        }

        http
          .post(`/board`, frm, {
            headers: {
              // contentType: 'multipart/form-data'
              "Content-Type": "multipart/form-data",
            },
          })
          .then(({ data }) => {
            let msg = "등록 처리시 문제가 발생했습니다.";
            if (data === "success") {
              msg = "등록이 완료되었습니다.";
            }
            alert(msg);
            if (this.userInfo.userId == "admin") {
              this.moveInform();
            } else {
              this.moveList();
            }
          });
      } else {
        http
          .post(`/board/write`, {
            userId: this.article.userId,
            subject: this.article.subject,
            content: this.article.content,
          })
          .then(({ data }) => {
            let msg = "등록 처리시 문제가 발생했습니다.";
            if (data === "success") {
              msg = "등록이 완료되었습니다.";
            }
            alert(msg);
            if (this.userInfo.userId == "admin") {
              this.moveInform();
            } else {
              this.moveList();
            }
          });
      }
    },
    moveList() {
      this.$router.push({ name: "boardlist" });
    },
    moveInform() {
      this.$router.push({ name: "inform" });
    },
  },
  computed: {
    ...mapState(memberStore, ["userInfo"]),
  },
};
</script>

<style scoped>
.main-container {
  padding-left: 15%;
  padding-right: 15%;
}
</style>
