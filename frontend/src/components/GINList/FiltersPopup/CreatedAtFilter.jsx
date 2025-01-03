import React, { useState, useEffect, useRef } from "react";
import dayjs from "dayjs"; // Đảm bảo bạn đã cài đặt dayjs package: npm install dayjs
import s from "./CreatedAtFilter.module.scss"; // Đảm bảo bạn đã import CSS cho file này
import cn from "classnames"; // Đảm bảo bạn đã cài đặt classnames package: npm install classnames
import { DatePicker, LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";

const CreatedAtFilter = ({
	closePopup,
	handeChangeDatafilter,
}) => {
	const popupRef = useRef(null);
	const [selectedRange, setSelectedRange] = useState(null);
	const [isSelectCustom, setIsSelectCustom] = useState(false);
	const [customStartDate, setCustomStartDate] = useState(null);
	const [customEndDate, setCustomEndDate] = useState(null);


	const dateRanges = {
		today: "Hôm nay",
		yesterday: "Hôm qua",
		lastWeek: "Tuần trước",
		thisWeek: "Tuần này",
		lastMonth: "Tháng trước",
		thisMonth: "Tháng này",
		custom: "Tùy chọn",
	};

	const [dateTempo, setDateTempo] = useState({
		createdAtmin: null,
		createdAtmax: null,
	});

	const dateRangesFunctions = {
		today: {
			start: () => dayjs().startOf("day"),
			end: () => dayjs().endOf("day"),
		},
		yesterday: {
			start: () => dayjs().subtract(1, "day").startOf("day"),
			end: () => dayjs().subtract(1, "day").endOf("day"),
		},
		lastWeek: {
			start: () => dayjs().subtract(1, "week").startOf("week"),
			end: () => dayjs().endOf("week"),
		},
		thisWeek: {
			start: () => dayjs().startOf("week"),
			end: () => dayjs().endOf("week"),
		},
		lastMonth: {
			start: () => dayjs().subtract(1, "month").startOf("month"),
			end: () => dayjs().subtract(1, "month").endOf("month"),
		},
		thisMonth: {
			start: () => dayjs().startOf("month"),
			end: () => dayjs().endOf("month"),
		},
		custom: {
			start: () => customStartDate,
			end: () => customEndDate,
		},
	};
	const handleDateRange = (range) => {
		setSelectedRange(range);

		if (range === "custom") {
			setCustomEndDate(null);
			setCustomStartDate(null);
			setIsSelectCustom(true);
		} else {
			setIsSelectCustom(false);
			setDateTempo({
				createdAtmin: dateRangesFunctions[range].start(),
				createdAtmax: dateRangesFunctions[range].end(),
			});
		}
	};

	const handleFilterClick = () => {
		if (dateTempo.createdAtmin && dateTempo.createdAtmax) {
			handeChangeDatafilter({ date_from: dayjs(dateTempo.createdAtmin).format("YYYY-MM-DD"), date_to: dayjs(dateTempo.createdAtmax).format("YYYY-MM-DD") });
			closePopup();
		}
	}

	useEffect(() => {
		if (isSelectCustom && customStartDate && customEndDate) {
			setDateTempo({
				createdAtmin: customStartDate,
				createdAtmax: customEndDate,
			});
		}
	}, [customStartDate, customEndDate, isSelectCustom]);


	return (
		<div ref={popupRef} className={s.container}>
			<div className={s.wrapper}>
				<div className={s.selectWrapper}>
					{Object.keys(dateRanges).map((range) => (
						<button
							key={range}
							className={cn(s.selectItem, {
								[s.selected]: selectedRange === range,
							})}
							onClick={() => handleDateRange(range)}
							style={
								range === "custom" ? { width: "calc(100% - 8px)" } : {}
							}
						>
							<span>{dateRanges[range]}</span>
						</button>
					))}
				</div>

				{isSelectCustom && (
					<div className={s.customContainer}>
						<div className="w-100">
							<div className={s.customAt}>
								<LocalizationProvider dateAdapter={AdapterDayjs}>
									<DatePicker
										value={customStartDate}
										onChange={(date) => setCustomStartDate(date)}

									/>
								</LocalizationProvider>
							</div>
						</div>

						<div
							style={{
								height: "1px",
								flex: "0 0 auto",
								background: "rgb(163, 168, 175)",
								width: "0.285714rem",
								margin: "0px 0.571429rem",
							}}
						></div>

						<div className="w-100">
							<div className={s.customAt}>
								<LocalizationProvider dateAdapter={AdapterDayjs}>
									<DatePicker
										value={customEndDate}
										onChange={(date) => setCustomEndDate(date)}

									/>
								</LocalizationProvider>
							</div>
						</div>
					</div>
				)}

				<button className={s.filter} onClick={handleFilterClick}>
					<span>Lọc</span>
				</button>
			</div>
		</div>
	);
};

export default CreatedAtFilter;
