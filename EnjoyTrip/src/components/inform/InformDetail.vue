<template>
  <div class="pt-4 main-container">
    <!-- 글 보기 -->
    <b-row class="justify-content-center view-title">
      <b-col lg="8" md="10" sm="12">
        <h2 class="my-3 py-3 shadow-sm bg-light text-center">글보기</h2>
      </b-col>
    </b-row>
    <!-- 게시글 -->
    <div class="view-board">
      <!-- 글 목록, 수정, 삭제 버튼 -->
      <b-row>
        <b-col cols="auto" class="mr-auto p-3">
          <b-button variant="outline-primary" @click="moveList">목록</b-button>
        </b-col>
        <div v-if="userInfo != null">
          <b-col v-if="userInfo.userId == article.userId" cols="auto" class="pt-3 pd-3">
            <b-button variant="outline-info" @click="modifyArticle">글수정</b-button>
            <b-button class="ml-2" variant="outline-danger" @click="deleteArticle">글삭제</b-button>
          </b-col>
        </div>
      </b-row>
      <!-- 게시글 내용 -->
      <b-row class="mb-1">
        <b-col>
          <b-card class="mb-2" border-variant="dark" no-body>
            <template #header>
              <h3>{{ article.articleNo }}.{{ article.subject }} [{{ article.hit }}]</h3>
              <div v-if="profileimage">
                <img :src="profileimage" style="width: 30px; height: 30px" />{{ article.userName }}
              </div>
              <div v-else>
                <b-avatar variant="secondary" size="sm"></b-avatar> {{ article.userName }}
              </div>
              <div>
                <h6>{{ article.registerTime }}</h6>
              </div>
            </template>
            <b-card-body class="text-left">
              <div v-html="message"></div>
            </b-card-body>
            <!-- 파일 첨부 -->
            <b-card-footer v-if="article.fileInfos">
              <a v-for="file in article.fileInfos" :key="file.originalFile">
                <div>
                  {{ file.originalFile }}
                  <a
                    href="#"
                    @click="filedown(file.saveFolder, file.saveFile, file.originalFile)"
                    :sfolder="file.saveFolder"
                    :sfile="file.saveFile"
                    :ofile="file.originalFile"
                    >[다운로드]</a
                  >
                </div>
              </a>
            </b-card-footer>
          </b-card>
        </b-col>
      </b-row>
    </div>

    <!-- 댓글 -->
    <!-- <div class="view-comment" style="margin-top: 16px; margin-bottom: 16px">
      <board-comment :articleNo="article.articleNo"></board-comment>
    </div> -->
  </div>
</template>

<script>
import http from "@/util/http-common.js";
import { mapState } from "vuex";
// import BoardComment from "./BoardComment.vue";

const memberStore = "memberStore";
export default {
  name: "InformDetail",
  components: {
    // BoardComment,
  },
  data() {
    return {
      article: {},
      profileimage: null,
    };
  },
  computed: {
    ...mapState(memberStore, ["userInfo"]),
    message() {
      if (this.article.content) return this.article.content.split("\n").join("<br>");
      return "";
    },
  },
  created() {
    http.get(`/board/${this.$route.params.articleNo}`).then(({ data }) => {
      this.article = data;
    });
    http
      .get(`/board/showImage/${this.$route.params.articleNo}`, { responseType: "blob" })
      .then((response) => {
        if (response.data.type === "application/json") {
          this.profileimage = "";
        } else {
          const blob = new Blob([response.data]);
          const fileObjectUrl = window.URL.createObjectURL(blob);
          this.profileimage = fileObjectUrl;
        }
      });
  },
  methods: {
    filedown(saveFolder, saveFile, originalFile) {
      http
        .post(
          `/board/download`,
          {
            sfolder: saveFolder,
            sfile: saveFile,
            ofile: originalFile,
          },
          { responseType: "blob" }
        )
        .then((response) => {
          const blob = new Blob([response.data]);
          const fileObjectUrl = window.URL.createObjectURL(blob);
          const link = document.createElement("a");
          link.href = fileObjectUrl;
          link.style.display = "none";
          link.download = originalFile;
          document.body.appendChild(link);
          link.click();
          link.remove();
          window.URL.revokeObjectURL(fileObjectUrl);
        });
    },
    modifyArticle() {
      this.$router.replace({
        name: "boardmodify",
        params: { articleNo: this.article.articleNo },
      });
      //   this.$router.push({ path: `/board/modify/${this.article.articleno}` });
    },
    deleteArticle() {
      if (confirm("정말로 삭제?")) {
        this.$router.replace({
          name: "boarddelete",
          params: { articleNo: this.article.articleNo },
        });
      }
    },
    moveList() {
      this.$router.push({ name: "informlist" });
    },
  },
};
</script>

<style scoped>
.main-container {
  padding-left: 15%;
  padding-right: 15%;
  /* padding: 100px 60px; */
}

/* .view-title {
    margin-left: 20%;
    margin-right: 20%;
} */

.view-board {
  margin-left: 15%;
  margin-right: 15%;
}

/* .view-comment {
  margin-left: 15%;
  margin-right: 15%;
} */
</style>
