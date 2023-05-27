import { apiInstance } from "./index.js";

const api = apiInstance();

async function getmaplist(search, success, fail) {
    await api.get(`/vmap/search/${search.area}/${search.areaarea}/${search.contentid}`).then(success).catch(fail);
}

async function getchartdata(select, success, fail) {
    await api.get(`/chart/${select}`).then(success).catch(fail);
}

export { getmaplist, getchartdata };
