/*
 * JavaScript file for the application to demonstrate
 * using the API
 */

// Create the namespace instance
let ns = {};

// Create the model instance
ns.model = (function() {
    'use strict';
    let $event_pump = $('body');

    // Return the API
    return {
        read: function() {
        	/*	let data=
			  {
				"month_id": "12",
				"month_name": "December",
				"year": "2019",
				"usershift": [
				{
					"user_id": "1",
					"shift_id":"1",
					"user_name": "Himanshu Sharma",
					"shift_name":"General",
					"shift_time":"0800-1630 IST",
					"weekoff": [2,3,9,10,16,17,23,24,30,31],
					"unplanned": [26,27],
					"leave": [4,19]
				},
				{
					"user_id": "2",
					"shift_id":"2",
					"user_name": "Deepak Singh",
					"shift_name":"UK",
					"shift_time":"0800-1630 IST",
					"weekoff": [4,5,11,12,18,19,25,26],
					"leave": [2,15]
				}
				]
			}
			;
			$event_pump.trigger('model_read_success', [data]);
			*/
			
            let ajax_options = {
                type: 'GET',
                url: '/shift',
                //accepts: 'application/json',
                //contentType: 'application/json',
                //dataType: 'json'
            };
            $.ajax(ajax_options)
            .done(function(data) {
                $event_pump.trigger('model_read_success', [data]);
            })
            .fail(function(xhr, textStatus, errorThrown) {
                $event_pump.trigger('model_error', [xhr, textStatus, errorThrown]);
            })			
			
        },
        readByMonthYear: function(month,year) {
        	let ajax_options = {
                type: 'GET',
                url: '/shift/'+month+'/'+year,
                //accepts: 'application/json',
                //contentType: 'application/json',
                //dataType: 'json'
            };
            $.ajax(ajax_options)
            .done(function(data) {
                $event_pump.trigger('model_read_success', [data]);
            })
            .fail(function(xhr, textStatus, errorThrown) {
                $event_pump.trigger('model_error', [xhr, textStatus, errorThrown]);
            })			
			
        },
        create: function(fname, lname) {
            let ajax_options = {
                type: 'POST',
                url: 'api/people',
                accepts: 'application/json',
                contentType: 'application/json',
                //dataType: 'json',
                data: JSON.stringify({
                    'fname': fname,
                    'lname': lname
                })
            };
            $.ajax(ajax_options)
            .done(function(data) {
                $event_pump.trigger('model_create_success', [data]);
            })
            .fail(function(xhr, textStatus, errorThrown) {
                $event_pump.trigger('model_error', [xhr, textStatus, errorThrown]);
            })
        },
        update: function(fname, lname) {
            let ajax_options = {
                type: 'PUT',
                url: 'api/people/' + lname,
                accepts: 'application/json',
                contentType: 'application/json',
                dataType: 'json',
                data: JSON.stringify({
                    'fname': fname,
                    'lname': lname
                })
            };
            $.ajax(ajax_options)
            .done(function(data) {
                $event_pump.trigger('model_update_success', [data]);
            })
            .fail(function(xhr, textStatus, errorThrown) {
                $event_pump.trigger('model_error', [xhr, textStatus, errorThrown]);
            })
        },
        'delete': function(lname) {
            let ajax_options = {
                type: 'DELETE',
                url: 'api/people/' + lname,
                accepts: 'application/json',
                contentType: 'plain/text'
            };
            $.ajax(ajax_options)
            .done(function(data) {
                $event_pump.trigger('model_delete_success', [data]);
            })
            .fail(function(xhr, textStatus, errorThrown) {
                $event_pump.trigger('model_error', [xhr, textStatus, errorThrown]);
            })
        }
    };
}());

// Create the view instance
ns.view = (function() {
    'use strict';

    let $month = $('#month'),
        $year = $('#year');

    // return the API
    return {
        /*reset: function() {
            $lname.val('');
            $fname.val('').focus();
        },*/
		getDatesInMonth: function(month, year) {
		  var date = new Date(year, month, 1);
		  var days = [];
		  while (date.getMonth() === month) {
			days.push(date.getDate());
			date.setDate(date.getDate() + 1);
		  }
		  return days;
		},
		getDaysInMonth: function(month, year) {
			var weekday = new Array(7);
			weekday[0] = "Sun";
			weekday[1] = "Mon";
			weekday[2] = "Tue";
			weekday[3] = "Wed";
			weekday[4] = "Thu";
			weekday[5] = "Fri";
			weekday[6] = "Sat";
			
		  var date = new Date(year, month, 1);
		  var days = [];
		  while (date.getMonth() === month) {
			days.push(weekday[date.getDay()]);
			date.setDate(date.getDate() + 1);
		  }
		  return days;
		},
        update_editor: function(fname, lname) {
            $lname.val(lname);
            $fname.val(fname).focus();
        },
        build_table: function(shifts) {
            let rows = ''
			//console.log("Month:"+shifts.month_id);
			//console.log("Year:"+shifts.year);
			let dates=this.getDatesInMonth(shifts.month_id-1,shifts.year);
			let days = this.getDaysInMonth(shifts.month_id-1,shifts.year);
			//console.log("days:"+dates);
			//console.log("days:"+days);
			//console.log(shifts.usershift[0].user_name);
			/*
			<tr>
			<td></td>
			<th scope="col">Monday</th>
			<th scope="col">Tuesday</th>
			<th scope="col">Wednesday</th>
			<th scope="col">Thursday</th>
			<th scope="col">Friday</th>
		  </tr>
		  */
            // clear the table
            $('.shifts table > tbody').empty();
			rows+=`<tr><td></td><td></td>`;
			for (let i=0, l=days.length; i < l; i++){
				rows = rows+`<th scope="col">`+days[i]+`</th>`;
			}
			rows+=`</tr>`;
			
			rows+=`<tr><td></td><td>Shift</td>`;
			for (let i=0, l=dates.length; i < l; i++){
				rows = rows+`<th scope="col">`+dates[i]+`</th>`;
			}
			rows+=`</tr>`;
			
			/*
			<tr>
				<th scope="row">09:00 - 11:00</th>
				<td>Closed</td>
				<td>Open</td>
				<td>Open</td>
				<td>Closed</td>
				<td>Closed</td>
			  </tr>
			*/
            // did we get a people array?
			let userShifts=shifts.usershift;
            if (shifts) {
				for (let i=0, l=userShifts.length; i < l; i++) {
					//console.log(userShifts[i]);
					rows = rows+`<tr><th scope="row">`+userShifts[i].user_name+`</th><td>`+userShifts[i].shift_name+`</td>`;
					for (let j=0, k=dates.length; j < k; j++){
						if(Array.isArray(userShifts[i].weekoff) && userShifts[i].weekoff.indexOf(dates[j]) > -1){
							rows=rows + `<td class="weekoff">WO</td>`;
						}else if(Array.isArray(userShifts[i].leave) && userShifts[i].leave.indexOf(dates[j]) > -1){
							rows=rows + `<td class="plan-leave">PL</td>`;
						}else if(Array.isArray(userShifts[i].unplannedLeave) && userShifts[i].unplannedLeave.indexOf(dates[j]) > -1){
							rows=rows + `<td class="unplan-leave">UL</td>`;
						}else{
							rows=rows + `<td class="weekday">ND</td>`;
						}
					}
					rows=rows + `</tr>`;
				}
				
				
				/*
				for (let i=0, l=people.length; i < l; i++) {
                    rows += `<tr><td class="fname">${people[i].fname}</td><td class="lname">${people[i].lname}</td><td>${people[i].timestamp}</td></tr>`;
                }
				*/
                $('table > tbody').append(rows);
            }
        },
        error: function(error_msg) {
            $('.error')
                .text(error_msg)
                .css('visibility', 'visible');
            setTimeout(function() {
                $('.error').css('visibility', 'hidden');
            }, 3000)
        }
    };
}());

// Create the controller
ns.controller = (function(m, v) {
    'use strict';
    let model = m,
        view = v,
        $event_pump = $('body'),
        $month = $('#month'),
        $year = $('#year');

    // Get the data from the model after the controller is done initializing
    setTimeout(function() {
        model.read();
    }, 100)

    // Validate input
    /*function validate(fname, lname) {
        return fname !== "" && lname !== "";
    }*/

    // Create our event handlers
    /*$('#create').click(function(e) {
        let fname = $fname.val(),
            lname = $lname.val();

        e.preventDefault();

        if (validate(fname, lname)) {
            model.create(fname, lname)
        } else {
            alert('Problem with first or last name input');
        }
    });

    $('#update').click(function(e) {
        let fname = $fname.val(),
            lname = $lname.val();

        e.preventDefault();

        if (validate(fname, lname)) {
            model.update(fname, lname)
        } else {
            alert('Problem with first or last name input');
        }
        e.preventDefault();
    });

    $('#delete').click(function(e) {
        let lname = $lname.val();

        e.preventDefault();

        if (validate('placeholder', lname)) {
            model.delete(lname)
        } else {
            alert('Problem with first or last name input');
        }
        e.preventDefault();
    });*/
    
    $('#submit').click(function(e) {

        e.preventDefault();
        //console.log('Inside submit'+$month.val()+$year.val());
        model.readByMonthYear($month.val(),$year.val());
        
        e.preventDefault();
    });

    /*$('#reset').click(function() {
        view.reset();
    })*/

    $('table > tbody').on('dblclick', 'tr', function(e) {
        let $target = $(e.target),
            fname,
            lname;

        fname = $target
            .parent()
            .find('td.fname')
            .text();

        lname = $target
            .parent()
            .find('td.lname')
            .text();

        view.update_editor(fname, lname);
    });

    // Handle the model events
    $event_pump.on('model_read_success', function(e, data) {
        view.build_table(data);
        //view.reset();
    });

    $event_pump.on('model_create_success', function(e, data) {
        model.read();
    });

    $event_pump.on('model_update_success', function(e, data) {
        model.read();
    });

    $event_pump.on('model_delete_success', function(e, data) {
        model.read();
    });

    $event_pump.on('model_error', function(e, xhr, textStatus, errorThrown) {
        let error_msg = textStatus + ': ' + errorThrown + ' - ' + xhr.responseJSON.detail;
        view.error(error_msg);
        console.log(error_msg);
    })
}(ns.model, ns.view));
