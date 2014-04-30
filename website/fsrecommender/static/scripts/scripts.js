$(document).ready(function() {
	$('.time-select input.hour').spinner({
		max: 23,
		min: 0
	});
	$('.time-select input.minute').spinner({
		max: 59,
		min: 0
	});
});