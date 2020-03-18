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
        getYears: function() {
        	let ajax_options = {
                type: 'GET',
                url: '/static/years'
            };
            $.ajax(ajax_options)
            .done(function(data) {
                $event_pump.trigger('model_years_success', [data]);
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
        getMonths: function() {
			var months=["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"];

		  return months;
		},
		addMonthDropDown: function(){
			let months=this.getMonths();
			let rows = ''
			$('#month').empty();

			rows = `<option value="0" default=true>-None-</option>`;
			for (let i=0, l=months.length; i < l; i++){
				rows = rows+`<option value="`+(i+1)+`">`+months[i]+`</option>`;
			}
			$('#month').append(rows);
		},
		addYearDropDown: function(data){
			let rows = ''
			$('#year').empty();
			rows = `<option value="0" default=true>-None-</option>`;
			for (let i=0, l=data.length; i < l; i++){
				rows = rows+`<option value="`+data[i]+`">`+data[i]+`</option>`;
			}
			$('#year').append(rows);
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
						var out=false;
						$.each(userShifts[i].exceptionData,function(key,value){							
							if(value.dates.indexOf(dates[j]) > -1){
								rows=rows + `<td class="`+value.excp_name+`">`+value.excp_name+`</td>`;
								out=true;
								return false;
							}
							
						});
						if(out == false)rows=rows + `<td class="WD">WD</td>`;
						/*if(!(userShifts[i].weekoff == null) && Array.isArray(userShifts[i].weekoff.dates) && userShifts[i].weekoff.dates.indexOf(dates[j]) > -1){
							rows=rows + `<td class="weekoff">WO</td>`;
							console.log('entering weekoff');
						}else if(!(userShifts[i].leave == null) && Array.isArray(userShifts[i].leave.dates) && userShifts[i].leave.dates.indexOf(dates[j]) > -1){
							rows=rows + `<td class="plan-leave">PL</td>`;
							console.log('entering leave');
						}else if(!(userShifts[i].unplannedLeave == null) && Array.isArray(userShifts[i].unplannedLeave.dates) && userShifts[i].unplannedLeave.dates.indexOf(dates[j]) > -1){
							rows=rows + `<td class="unplan-leave">UL</td>`;
							console.log('entering unplan');
						}else if(!(userShifts[i].specialLeave == null) && Array.isArray(userShifts[i].specialLeave.dates) && userShifts[i].specialLeave.dates.indexOf(dates[j]) > -1){
							rows=rows + `<td class="special-leave">SL</td>`;
							console.log('entering special');
						}*/
						
						/*else{
							
							rows=rows + `<td class="weekday">WD</td>`;
						}*/
					}
					rows=rows + `</tr>`;
				}
				
				
				/*
				for (let i=0, l=people.length; i < l; i++) {
                    rows += `<tr><td class="fname">${people[i].fname}</td><td class="lname">${people[i].lname}</td><td>${people[i].timestamp}</td></tr>`;
                }
				*/
                $('.shifts table > tbody').append(rows);
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
    	view.addMonthDropDown();
    }, 100);
    
    $("#month").change(function() {
  	  model.getYears();
  	});
    $("#year").change(function() {
    	if($('#month').val() != 0 && $('#year').val() != 0){
    		model.readByMonthYear($('#month').val(),$('#year').val());
    		//$('.btn-update').show();
    		//$('.btn-generate').show();
    	}else{
    			$('.alert').show();
    			$('.message').html("Select Month and Year");
    			setTimeout(function() { // this will automatically close in 5 secs
    			      $(".alert").hide();
    			    }, 5000);
    	}  	  
  	});

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
    
    $('#create').click(function(e) {

            //alert("This is button on click event.");
            top.location.href = '/edit';
    });

    /*$('#reset').click(function() {
        view.reset();
    })*/

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
    
    $event_pump.on('model_years_success', function(e, data) {
        view.addYearDropDown(data);
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
