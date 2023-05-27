<template>
  <div>
    <add-star-modal ref="addStarModal" :modalContent="modalContent"></add-star-modal>
    <a v-if="!markerinfo.length">검색 결과가 존재하지 않습니다.</a>
    <b-list-group>
      <b-list-group-item
        class="card"
        v-for="marker in markerinfo"
        :key="marker.contentId"
        @click="moveMap(marker)"
      >
        <b-row cols="1" cols-sm="1" cols-md="2" cols-lg="2" cols-xl="2">
          <b-col><b-img :src="marker.firstImage" fluid></b-img></b-col>
          <b-col align-self="center">{{ marker.title }}</b-col>
        </b-row>
        <b-row cols="1">
          <b-col>{{ marker.addr1 }} {{ marker.addr2 }}</b-col>
        </b-row>
        <b-col>
          <b-button v-if="isLogin" @click="openModal(marker)">추가</b-button>
          <b-button @click="goTrip(marker)">보기</b-button>
        </b-col>
      </b-list-group-item>
    </b-list-group>
  </div>
</template>

<script>
import AddStarModal from "./AddStarModal.vue";
import { mapState } from "vuex";

const memberStore = "memberStore";

export default {
  data() {
    return {
      modalContent: null,
    };
  },
  components: {
    AddStarModal,
  },
  props: {
    markerinfo: Array,
  },
  computed: {
    ...mapState(memberStore, ["isLogin"]),
  },
  methods: {
    openModal(marker) {
      this.modalContent = marker;
      this.$refs.addStarModal.$refs.addStarModal.show();
    },
    moveMap(trip) {
      console.log(trip);
      this.$emit("moveMap", trip);
    },
    goTrip(trip) {
      console.log(trip);
      this.$router.push({ name: "tripdetail", params: { contentid: `${trip.contentId}` } });
    },
  },
};
</script>

<style scoped>
.card {
  height: 220px;
}
</style>
