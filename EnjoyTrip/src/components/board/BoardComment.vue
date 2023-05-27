<template>
  <b-list-group style="text-align: left">
    <b-list-group-item variant="secondary" style="font-weight: bold; font-size: 18px">
      <b-row>
        <b-col cols="auto" class="mr-auto" style="padding-top: 6px">댓글</b-col>
        <b-col cols="auto">
          <b-button variant="dark" @click="setWriteFlag">+</b-button>
        </b-col>
      </b-row>
    </b-list-group-item>
    <!-- 댓글 작성 창 -->
    <b-list-group-item v-if="commentWriteFlag">
      <b-row>
        <b-col cols="auto" class="mr-auto" style="font-weight: bold; font-size: 15px">
          {{ this.userInfo.userId }}
        </b-col>
      </b-row>
      <!-- 댓글 input -->
      <b-row style="margin-top: 10px">
        <b-col>
          <b-form-input v-model="content" autofocus></b-form-input>
        </b-col>
      </b-row>
      <b-row style="margin-top: 10px; float: right">
        <b-col>
          <b-button @click="writeComment" size="sm">등록</b-button>
        </b-col>
      </b-row>
    </b-list-group-item>
    <!-- 댓글 -->
    <b-list-group-item v-for="comment in comments" :key="comment.commentNo">
      <!-- 작성자 아이디, 작성시간, 수정, 삭제 -->
      <b-row>
        <!-- 작성자 아이디 -->
        <b-col cols="auto" class="mr-auto" style="font-weight: bold; font-size: 15px">
          {{ comment.userId }}
        </b-col>
        <!-- 작성 시간 -->
        <b-col cols="auto">{{ comment.registerTime }}</b-col>
        <!-- 수정, 삭제 버튼 -->
        <b-col cols="auto" v-if="isLogin" style="padding-left: 0">
          <a
            class="col-right"
            v-if="comment.userId == userInfo.userId"
            @click="modifyComment"
            style="color: gray"
            >수정</a
          >
          <a
            class="col-right"
            v-if="comment.userId == userInfo.userId || userInfo.userId === 'admin'"
            @click="deleteComment(comment.commentNo)"
            style="color: gray"
          >
            삭제
          </a>
        </b-col>
      </b-row>
      <!-- 댓글 내용 -->
      <b-row v-if="!commentModifyFlag" style="margin-top: 10px">
        <b-col>
          {{ comment.comment }}
        </b-col>
      </b-row>
      <!-- 댓글 수정 -->
      <b-row v-if="commentModifyFlag" style="margin-top: 10px">
        <b-col cols="auto" class="mr-auto" style="flex-grow: 1">
          <b-form-input v-model="comment.comment" autofocus></b-form-input>
        </b-col>
        <b-col cols="auto">
          <b-button @click="modifySubmit(comment)" size="sm">완료</b-button>
        </b-col>
      </b-row>
    </b-list-group-item>
  </b-list-group>
</template>

<script>
import http from "@/util/http-common.js";
import { mapState } from "vuex";
// import BoardCommentItem from "./item/BoardCommentItem.vue";

const memberStore = "memberStore";

export default {
  name: "",
  props: {
    articleNo: Number,
  },
  components: {},
  data() {
    return {
      commentWriteFlag: false,
      commentModifyFlag: false,
      comments: [],
      content: "",
    };
  },
  created() {
    http.get(`/comment/list/${this.$route.params.articleNo}`).then(({ data }) => {
      this.comments = data;
    });
  },
  computed: {
    ...mapState(memberStore, ["isLogin", "userInfo"]),
  },
  methods: {
    setWriteFlag() {
      this.commentWriteFlag = !this.commentWriteFlag;
      console.log(this.comments);
    },
    writeComment() {
      //   console.log(this.articleNo);
      http
        .post(`/comment`, {
          articleNo: this.articleNo,
          userId: this.userInfo.userId,
          comment: this.content,
        })
        .then(({ data }) => {
          if (data === "success") {
            http.get(`/comment/list/${this.$route.params.articleNo}`).then(({ data }) => {
              this.comments = data;
            });
          }
        });
      this.reset();
    },
    reset() {
      this.commentWriteFlag = false;
      this.content = "";
    },
    modifyComment() {
      this.commentModifyFlag = !this.commentModifyFlag;
    },
    modifySubmit(comment) {
      this.commentModifyFlag = false;
      http.put("/comment", comment).then(({ data }) => {
        if (data === "success") {
          alert("수정 성공");
        }
      });
    },
    deleteComment(commentNo) {
      http.delete(`/comment/${commentNo}`).then(({ data }) => {
        let msg = "삭제 실패";
        if (data === "success") {
          msg = "삭제되었습니다.";
          http.get(`/comment/list/${this.$route.params.articleNo}`).then(({ data }) => {
            this.comments = data;
          });
        }
        alert(msg);
      });
    },
  },
};
</script>

<style scoped>
.col-right:hover {
  color: gray;
}
</style>
