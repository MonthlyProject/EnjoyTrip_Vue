<template>
  <div>
    <h1 class="title">여행 계획</h1>
    <b-row style="margin-left: 10%">
      <b-col cols="3"
        ><star-list @setmarkerinfo="setmarkerinfo" @moveMap="moveMap"></star-list
      ></b-col>
      <b-col cols="8"
        ><kakao-map :markerinfo="markerinfo" :movelocation="movelocation"></kakao-map
      ></b-col>
    </b-row>
  </div>
</template>

<script>
import KakaoMap from "@/components/map/KakaoMap.vue";
import http from "@/util/http-common.js";
import StarList from "@/components/star/StarList.vue";

export default {
  name: "StarView",
  data() {
    return {
      areas: null,
      areaareas: null,
      area: 0,
      areaarea: 0,
      contentid: 0,
      markerinfo: [],
      movelocation: null,
    };
  },
  components: {
    KakaoMap,
    StarList,
  },
  created() {
    http.get("/vmap/area").then(({ data }) => (this.areas = data));
  },
  methods: {
    search() {
      http.get(`/map/search/${this.area}/${this.areaarea}/${this.contentid}`).then(({ data }) => {
        this.markerinfo = data;
        console.log(this.markerinfo);
      });
    },
    setmarkerinfo(triplist) {
      this.markerinfo = triplist;
    },
    moveMap(trip) {
      this.movelocation = trip;
    },
  },
  watch: {
    area(newarea) {
      http.get("/vmap/" + newarea).then(({ data }) => {
        console.log(newarea);
        this.areaareas = data;
      });
    },
  },
};
</script>

<style>
.title {
  color: #008374;
  font-size: 45px;
  font-weight: 700;
  margin-top: 60px;
  margin-bottom: 30px;
}

.drop-box {
  width: 300px;
  height: 60px;
  margin-right: 10px;
  border-color: rgb(212, 212, 212);
  border-radius: 10px;
  padding-left: 10px;
}
</style>
