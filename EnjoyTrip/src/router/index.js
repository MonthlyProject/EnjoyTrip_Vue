import Vue from "vue";
import VueRouter from "vue-router";
import HomeView from "../views/HomeView.vue";
import MapView from "../views/MapView";
import StarView from "../views/StarView";
// import Profile from "../components/user/profile/Profile.vue";
import TripDetail from "../views/TripDetail";
import ManageUser from "../views/ManageUser";
import ChartView from "../views/ChartView";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "home",
    component: HomeView,
  },
  {
    path: "/map",
    name: "map",
    component: MapView,
  },
  {
    path: "/profile",
    name: "profile",
    // component: Profile,
    component: () => import("@/components/user/profile/Profile"),
  },
  {
    path: "/board",
    name: "board",
    component: () => import("@/views/BoardView"),
    redirect: "/board/list",
    children: [
      {
        path: "list",
        name: "boardlist",
        component: () => import("@/components/board/BoardList"),
      },
      {
        path: "write",
        name: "boardwrite",
        component: () => import("@/components/board/BoardWrite"),
      },
      {
        path: "view/:articleNo",
        name: "boardview",
        component: () => import("@/components/board/BoardDetail"),
      },
      {
        path: "modify",
        name: "boardmodify",
        component: () => import("@/components/board/BoardModify"),
      },
      {
        path: "delete/:articleno",
        name: "boarddelete",
        component: () => import(/* webpackChunkName: "board" */ "@/components/board/BoardDelete"),
      },
    ],
  },
  {
    path: "/star",
    name: "star",
    component: StarView,
  },
  {
    path: "/inform",
    name: "inform",
    component: () => import("@/views/InformView"),
    redirect: "/inform/list",
    children: [
      {
        path: "list",
        name: "informlist",
        component: () => import("@/components/inform/InformList"),
      },
      {
        path: "view/:articleNo",
        name: "informview",
        component: () => import("@/components/inform/InformDetail"),
      },
    ],
  },
  {
    path: "/tripdetail/:contentid",
    name: "tripdetail",
    component: TripDetail,
  },
  {
    path: "/admin",
    name: "manage",
    component: ManageUser,
  },
  {
    path: "/chart",
    name: "chart",
    component: ChartView,
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
