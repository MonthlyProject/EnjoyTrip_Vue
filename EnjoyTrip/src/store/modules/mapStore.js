import { getchartdata, getmaplist } from "@/api/map";

const mapStore = {
    namespaced: true,
    state: {
        sido: 0,
        gugun: 0,
        contentType: 0,
        markerinfo: [],
        selected: "sido",
        chartData: {
            labels: [ 'January', 'February', 'March' ],
            datasets: [ { data: [40, 20, 12] } ]
        },
        chartOptions: {
            responsive: true
        }
    },
    mutations: {
        SET_SEARCH_BOX: (state, data) => {
            state.sido = data.area;
            state.gugun = data.areaarea;
            state.contentType = data.contentid;
        },
        SET_MAP_LIST: (state, data) => {
            state.markerinfo = data;
        },
        SET_CHART_DATA: (state, data, select) => {
            state.chartData.labels = data.labels;
            state.chartData.datasets[0].data = data.data;
            state.selected = select;
        }
    },
    actions: {
        async getMapList({ commit }, search) {
            await getmaplist(
                search,
                ({ data }) => {
                    if (data.message === "success") {
                        commit("SET_MAP_LIST", data.mapList);
                    } else {
                        console.log("데이터 불러오기 실패");
                    }
                },
                (error) => {
                    console.log(error);
                }
            );
        },
        async getChartData({ commit }, select) {
            await getchartdata(
                select,
                ({ data }) => {
                    commit("SET_CHART_DATA", data, select);
                },
                (error) => {
                    console.log(error);
                }
            );
        },
    },
};

export default mapStore;
