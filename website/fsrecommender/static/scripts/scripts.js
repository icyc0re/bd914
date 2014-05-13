// Helpers:
function hourAndMin(time) {
	var parts = time.split(":");
	var h = parseInt(parts[0]);
	var m = parseInt(parts[1]);
	return {hour: h, min: m};
}
function minsToTime(mins) {
	var h = Math.floor(mins/60);
	var m = mins%60;
	return (h < 10 ? '0'+h : h)+":"+(m < 10 ? '0'+m : m);
}
function timeToMins(time) {
	var hm = hourAndMin(time);
	return 60*hm.hour + hm.min;
}

function onSlide(event, ui) {
	var $parent = $(this).closest(".time-select");
	var vals = ui.values;

	var t1 = minsToTime(vals[0]);
	var t2 = minsToTime(vals[1]);
	$parent.find('.display').text('From ' + t1 + ' to ' + t2);
	$parent.find('input[name="time1"]').val(t1);
	$parent.find('input[name="time2"]').val(t2);
}

function getCurrentLocation() {
	// Stub:
	return [40.72,-73.99];
}

$(document).ready(function() {
	$('.time-select .element').slider({
		range: true,
		step: 1,
		max: 24*60-1,
		min: 0,
		slide: onSlide,
		values: [timeToMins("8:00"), timeToMins("22:00")]
			
	});
	$('.time-select').find('.display').text('From 08:00 to 22:00');
	$('.time-select').find('input[name="time1"]').val("08:00");
	$('.time-select').find('input[name="time2"]').val("22:00");

	$('.slider .element').slider({
		min: 0.1,
		max: 15,
		value: 2,
		step: .1,
		slide: function(event, ui) {
			var $parent = $(this).closest(".slider");
			var km = ui.value;
			$parent.find('.kms').text(km + ' km');

			if($parent.attr('data-callback')) {
				window[$parent.attr('data-callback')](km);
			}
		}
	});

	// Revealer code:
	$('a.revealer').each(function() {
		var $target = $($(this).attr("href"));
		$target.slideUp(0);
		$target.find("input").attr("disabled", true);
	});
	$('a.revealer').click(function() {
		if ($(this).hasClass('disabled')) {
			return false;
		}

		var $target = $($(this).attr("href"));
		
		$target.slideDown(100, function() {
			if($target.attr('data-callback')) {
				window[$target.attr('data-callback')]();
			}
		});
		$target.find("input").removeAttr("disabled");

		$(this).addClass('disabled');

		return false;
	})
});