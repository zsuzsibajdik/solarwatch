import { useMemo, useState } from "react";
import { getSolarWatch } from "../services/solarWatchService";
import AlertBanner from "./AlertBanner";

function isoToday() {
	return new Date().toISOString().slice(0, 10);
}

export default function SolarWatchPage() {
	const [city, setCity] = useState("");
	const [date, setDate] = useState(isoToday());
	const [timezone, setTimezone] = useState("LOCAL");

	const [loading, setLoading] = useState(false);
	const [error, setError] = useState(null);
	const [result, setResult] = useState(null);

	const canSubmit = useMemo(() => city.trim().length > 0 && !loading, [city, loading]);

	async function onSubmit(e) {
		e.preventDefault();
		setError(null);
		setResult(null);

		if (!city.trim()) return;

		setLoading(true);
		try {
			const data = await getSolarWatch(city.trim(), date, timezone);
			setResult(data);
		} catch (err) {
			const msg = err?.response?.data?.message || err?.message || "Request failed";
			setError(msg);
		} finally {
			setLoading(false);
		}
	}

	return (
		<div className="space-y-6">
			<div>
				<h1 className="text-2xl sm:text-3xl font-bold">Solar Watch</h1>
				<p className="text-base-content/70">
					Search by city to get sunrise/sunset for a specific day.
				</p>
			</div>

			<div className="card bg-base-100 shadow">
				<div className="card-body">
					<h2 className="card-title">Search</h2>
					<form onSubmit={onSubmit} className="mt-2 space-y-4">
						<div className="form-control">
							<label className="label">
								<span className="label-text">City</span>
							</label>
							<input
								className="input input-bordered"
								type="text"
								value={city}
								onChange={(e) => setCity(e.target.value)}
								placeholder="e.g. Budapest"
								autoComplete="off"
							/>
						</div>

						<div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
							<div className="form-control">
								<label className="label">
									<span className="label-text">Date</span>
								</label>
								<input
									className="input input-bordered"
									type="date"
									value={date}
									onChange={(e) => setDate(e.target.value)}
								/>
							</div>

							<div className="form-control">
								<label className="label">
									<span className="label-text">Timezone</span>
								</label>
								<select
									className="select select-bordered"
									value={timezone}
									onChange={(e) => setTimezone(e.target.value)}
								>
									<option value="LOCAL">LOCAL</option>
									<option value="UTC">UTC</option>
								</select>
							</div>
						</div>

						<button className="btn btn-primary" type="submit" disabled={!canSubmit}>
							{loading ? (
								<span className="inline-flex items-center gap-2">
									<span className="loading loading-spinner" />
									Loading…
								</span>
							) : (
								"Get sunrise & sunset"
							)}
						</button>
					</form>
				</div>
			</div>

			{error ? (
				<AlertBanner type="error" title="Request failed">
					{String(error)}
				</AlertBanner>
			) : null}

			{result ? (
				<div className="grid grid-cols-1 lg:grid-cols-3 gap-4">
					<div className="card bg-base-100 shadow lg:col-span-1">
						<div className="card-body">
							<h2 className="card-title">Summary</h2>
							<div className="stats stats-vertical shadow bg-base-200">
								<div className="stat">
									<div className="stat-title">City</div>
									<div className="stat-value text-xl">{result.city}</div>
								</div>
								<div className="stat">
									<div className="stat-title">Date</div>
									<div className="stat-value text-xl">{result.date}</div>
								</div>
								<div className="stat">
									<div className="stat-title">Timezone</div>
									<div className="stat-value text-xl">{result.timezone}</div>
								</div>
							</div>
						</div>
					</div>

					<div className="card bg-base-100 shadow lg:col-span-2">
						<div className="card-body">
							<h2 className="card-title">Times</h2>
							<div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
								<div className="card bg-base-200 border border-base-300">
									<div className="card-body">
										<div className="text-sm text-base-content/70">Sunrise</div>
										<div className="text-2xl font-semibold">{result.sunrise}</div>
									</div>
								</div>
								<div className="card bg-base-200 border border-base-300">
									<div className="card-body">
										<div className="text-sm text-base-content/70">Sunset</div>
										<div className="text-2xl font-semibold">{result.sunset}</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			) : null}
		</div>
	);

}
