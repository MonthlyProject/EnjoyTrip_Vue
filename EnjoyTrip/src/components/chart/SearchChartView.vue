<template>
  <div>
    <b-form-select
      style="width: 300px"
      align-h="center"
      :options="options"
      v-model="select"
    ></b-form-select>
    <div style="width: 100%px">
      <Bar ref="chart" id="my-chart-id" :options="chartOptions" :data="chartData" />
    </div>
  </div>
</template>

<script>
import { Bar } from "vue-chartjs";
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale,
} from "chart.js";
import { mapState, mapActions } from "vuex";

const mapStore = "mapStore";

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale);

export default {
  components: { Bar },
  data() {
    return {
      select: "0",
      options: [
        { text: "선택해주세요", value: "0" },
        { text: "시도", value: "sido" },
        { text: "구군", value: "gugun" },
        { text: "관광지 유형", value: "content" },
      ],
    };
  },
  methods: {
    ...mapActions(mapStore, ["getChartData"]),
  },
  computed: {
    ...mapState(mapStore, ["chartOptions", "chartData", "selected"]),
  },
  watch: {
    select() {
      this.getChartData(this.select).then(() => {
        this.$router.go();
        console.log(this.chartData);
      });
    },
  },
};
</script>

<style></style>
