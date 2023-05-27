<template>
  <b-modal ref="addStarModal" title="즐겨찾기 추가" @ok="addStar">
    {{ modalcontent.title }}
    <br />
    그룹:
    <select id="grout-title" v-model="grouptitle">
      <option value="0" selected>검색 할 지역 선택</option>
      <option v-for="group in groups" :key="group" :value="group">{{ group }}</option>
    </select>
    <b-input v-if="showinsert" v-model="createtitle"></b-input>
    <b-button v-if="showinsert" @click="addnewGroup">추가</b-button>
    <button v-if="!showinsert" @click="showInsert">+</button>
  </b-modal>
</template>

<script>
import http from "@/util/http-common";
import { mapState } from "vuex";

const memberStore = "memberStore";

export default {
  data() {
    return {
      grouptitle: 0,
      showinsert: false,
      createtitle: null,
      groups: [],
      showmodal: false,
      modalcontent: [],
    };
  },
  props: {
    modalContent: Object,
  },
  created() {
    if (this.isLogin) {
      http.get(`/vmap/star/group/` + this.userInfo.userId).then(({ data }) => {
        console.log(data);
        this.groups = data;
      });
    }
  },
  methods: {
    addStar() {
      console.log(this.modalContent.contentId);
      http
        .post(`/vmap`, {
          contentId: this.modalContent.contentId,
          userId: this.userInfo.userId,
          title: this.grouptitle,
        })
        .then(({ data }) => {
          let msg = "등록 처리시 문제가 발생했습니다.";
          if (data === "success") {
            msg = "등록이 완료되었습니다.";
          }
          alert(msg);
        });
    },
    showInsert() {
      this.showinsert = true;
    },
    addnewGroup() {
      this.showinsert = false;
      http
        .post(`/vmap/star/group`, {
          title: this.createtitle,
          userId: this.userInfo.userId,
        })
        .then(({ data }) => {
          if (data === "success") {
            this.groups.push(this.createtitle);
          }
        });
    },
  },
  watch: {
    modalContent() {
      this.modalcontent = this.modalContent;
    },
  },
  computed: {
    ...mapState(memberStore, ["isLogin", "userInfo"]),
  },
};
</script>

<style></style>
