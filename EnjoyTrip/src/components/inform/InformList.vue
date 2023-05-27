<template>
  <div class="pt-4 main-container">
    <b-row align-h="center" class="mb-4">
      <h2 class="font-weight-bold">공지사항</h2>
    </b-row>
    <div class="pt-3">
      <!-- 관리자만 편집 버튼 보이게 만들기//로그인 이후 -->
      <!-- 글쓰기 버튼, 검색 영역-->
      <b-row>
        <!-- 글쓰기 버튼 -->
        <div v-if="userInfo != null">
          <b-col v-if="userInfo.userId === 'admin'" cols="auto" class="mr-auto p-3">
            <button type="button" @click="moveWrite" class="btn btn-secondary">글쓰기</button>
          </b-col>
        </div>
        <!-- 검색 옵션 -->
        <b-col cols="auto" class="p-3">
          <b-form-group id="search-type" label-for="input-3">
            <b-form-select
              id="search"
              v-model="form.type"
              :options="options"
              required="required"
            ></b-form-select>
          </b-form-group>
        </b-col>
        <!-- 검색어 입력-->
        <b-col cols="auto" class="p-3">
          <b-form-input placeholder="검색어 입력" v-model="form.word"></b-form-input>
        </b-col>
        <!-- 검색 버튼-->
        <b-col cols="auto" class="p-3">
          <b-button type="submit" @click="searchArticle" variant="outline-primary">검색</b-button>
        </b-col>
      </b-row>

      <!-- 게시글 영역 -->
      <b-row>
        <b-col>
          <b-table
            striped="striped"
            hover="hover"
            :items="articles"
            :fields="fields"
            @row-clicked="viewArticle"
          >
            <template #cell(subject)="data">
              <router-link :to="{ name: 'informview', params: { articleNo: data.item.articleNo } }">
                {{ data.item.subject }}
              </router-link>
            </template>
          </b-table>
        </b-col>
      </b-row>

      <b-pagination
        v-model="currentPage"
        :total-rows="rows"
        :per-page="perPage"
        aria-controls="my-table"
        align="center"
      ></b-pagination>
    </div>
  </div>
</template>

<script>
import http from "@/util/http-common.js";
import { mapState } from "vuex";

const memberStore = "memberStore";

export default {
  name: "InformList",
  data() {
    return {
      currentPage: 1,
      perPage: 20,
      form: {
        type: null,
        word: "",
      },
      options: [
        {
          text: "검색조건",
          value: null,
        },
        {
          text: "글번호",
          value: "article_no",
        },
        {
          text: "제목",
          value: "subject",
        },
        {
          text: "작성자",
          value: "user_id",
        },
      ],
      articles: [],
      fields: [
        {
          key: "articleNo",
          label: "글번호",
          tdClass: "tdClass",
        },
        {
          key: "subject",
          label: "제목",
          tdClass: "tdSubject",
        },
        {
          key: "userId",
          label: "작성자",
          tdClass: "tdClass",
        },
        {
          key: "registerTime",
          label: "작성일",
          tdClass: "tdClass",
        },
        {
          key: "hit",
          label: "조회수",
          tdClass: "tdClass",
        },
      ],
    };
  },
  created() {
    http.get(`/board/inform/list`).then(({ data }) => {
      this.articles = data;
    });
  },
  methods: {
    moveWrite() {
      this.$router.push({ name: "boardwrite" });
    },
    viewArticle(article) {
      this.$router.push({
        name: "boardview",
        params: {
          articleNo: article.articleNo,
        },
      });
    },
    searchArticle() {
      // console.log(this.form.type); console.log(this.form.word);
      if (this.form.word.replace(/^\s+|\s+$/g, '') !== "") {
        http.get(`/board/inform/list/${this.form.type}/${this.form.word}`).then(({ data }) => {
          this.articles = data;
          // console.log(data);
        });
      }
    },
  },
  computed: {
    ...mapState(memberStore, ["userInfo"]),
    rows() {
      return this.articles.length;
    },
  },
};
</script>

<style scoped="scoped">
.main-container {
  padding-left: 15%;
  padding-right: 15%;
}

.tdClass {
  width: 50px;
  text-align: center;
}
.tdSubject {
  width: 300px;
  text-align: left;
}
</style>
