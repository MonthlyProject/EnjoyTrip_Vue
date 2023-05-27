<template>
    <div>
        <h1 class="title">관광지 조회</h1>
        <b-row align-h="center" style="margin: 10px;">
            <form class="d-flex">
                <select id="search-area" class="drop-box" v-model="area">
                    <option value="0" selected>검색 할 지역 선택</option>
                    <option v-for="area in areas" :key="area.sidoCode" :value="area.sidoCode">{{ area.sidoName }}</option>
                </select>
                <select id="search-area-area" class="drop-box" v-model="areaarea">
                    <option value="0" selected>검색 할 세부지역 선택</option>
                    <option v-for="areaarea in areaareas" :key="areaarea.gugunCode" :value="areaarea.gugunCode">{{ areaarea.gugunName }}</option>
                </select>
                <select id="search-content-id" class="drop-box" v-model="contentid">
                    <option value="0" selected>관광지 유형</option>
                    <option value="12">관광지</option>
                    <option value="14">문화시설</option>
                    <option value="15">축제공연행사</option>
                    <option value="25">여행코스</option>
                    <option value="28">레포츠</option>
                    <option value="32">숙박</option>
                    <option value="38">쇼핑</option>
                    <option value="39">음식점</option>
                </select>
                <button id="btn-search" class="btn btn-outline-success"
                    type="button" @click="search">검색</button>
            </form>
        </b-row>
        <b-row style="margin-left: 10%">
            <b-col cols="3" style="height: 670px; overflow-y: scroll;"><map-list :markerinfo="markerinfo" @moveMap="moveMap"></map-list></b-col>
            <b-col cols="8"><kakao-map :markerinfo="markerinfo" :movelocation="movelocation"></kakao-map></b-col>
        </b-row>
    </div>
</template>

<script>
import KakaoMap from "@/components/map/KakaoMap.vue";
import http from '@/util/http-common.js';
import MapList from '@/components/map/MapList.vue';
import { mapMutations, mapState, mapActions } from 'vuex';

let mapStore = "mapStore";

export default {
    name: "MapView",
    data() {
        return {
            areas: null,
            areaareas: null,
            area: 0,
            areaarea: 0,
            contentid: 0,
            movelocation: null,
        }
    },
    components: {
        KakaoMap,
        MapList,
    },
    created() {
        this.area = this.sido;
        this.areaarea = this.gugun;
        this.contentid = this.contentType;
        console.log(this.sido, this.gugun, this.contentType);
        http.get('/vmap/area')
            .then(({data}) => this.areas = data);
    },
    methods: {
        ...mapActions(mapStore, ["getMapList"]),
        ...mapMutations(mapStore, { setSearch: "SET_SEARCH_BOX" }),
        async search() {
            let search = { area: this.area, areaarea: this.areaarea, contentid: this.contentid };
            this.setSearch(search);
            await this.getMapList(search);
        },
        moveMap(trip) {
            this.movelocation = trip;
        }
    },
    watch: {
        area(newarea) {
            http.get('/vmap/' + newarea)
                .then(({ data }) => {
                    console.log(newarea);
                    this.areaareas = data;
                });
        },
    },
    computed: {
        ...mapState(mapStore, ["sido", "gugun", "contentType", "markerinfo"]),
    },
};
</script>

<style>
.title{
    color: #008374;
    font-size: 45px;
    font-weight: 700;
    margin-top: 60px;
    margin-bottom: 30px;
}

.drop-box{
    width: 300px;
    height: 60px;
    margin-right: 10px;
    border-color: rgb(212, 212, 212);
    border-radius: 10px;
    padding-left: 10px;
}
</style>
