import { axiosPrivate } from "../api/axios.js";

export async function getSolarWatch(city, date, timezone = "LOCAL") {
	const response = await axiosPrivate.get("/solar-watch", {
		params: { city, date, timezone }
	});
	return response.data;
}

