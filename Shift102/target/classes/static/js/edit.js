/*
 * JavaScript file for the application to demonstrate
 * using the API
 */

// Create the namespace instance
let ns = {};
ns.eventBound=0;


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
        getUsers: function() {
        	let ajax_options = {
                type: 'GET',
                url: '/static/users'
            };
            $.ajax(ajax_options)
            .done(function(data) {
                ns.users=data;
                //console.log(ns.users);
            })
            .fail(function(xhr, textStatus, errorThrown) {
                $event_pump.trigger('model_error', [xhr, textStatus, errorThrown]);
            })			
			
        },
        getShifts: function() {
        	let ajax_options = {
                type: 'GET',
                url: '/static/shifts'
            };
            $.ajax(ajax_options)
            .done(function(data) {
                ns.shifts=data;
                //console.log(ns.shifts);
            })
            .fail(function(xhr, textStatus, errorThrown) {
                $event_pump.trigger('model_error', [xhr, textStatus, errorThrown]);
            })			
			
        },
        loadExistingData: function(month,year) {
        	let ajax_options = {
                    type: 'GET',
                    url: '/shift/'+month+'/'+year,
                };
                $.ajax(ajax_options)
                .done(function(data) {
                    $event_pump.trigger('model_editload_success', [data]);
                })
                .fail(function(xhr, textStatus, errorThrown) {
                    $event_pump.trigger('model_error', [xhr, textStatus, errorThrown]);
                })			
    			
            },

        create: function(data) {
            let ajax_options = {
                type: 'POST',
                url: '/shift/update',
                /*accepts: 'application/json',*/
                //dataType: "json",
                contentType: "application/json",
                data: data
            };
            $.ajax(ajax_options)
            .done(function(data) {
                $event_pump.trigger('model_update_success', [data]);
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

    //let $month = $('#month'), $year = $('#year');

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
		rotateClass: function(element) {
			var classes = ['weekday', 'weekoff', 'plan-leave', 'unplan-leave'];
			var innerText = ['WD', 'WO', 'PL', 'UL'];
			//console.log('current class:'+element.attr("class"));
			var currentClass=element.attr("class").replace('datetoggle','').trim();
			//console.log('::'+currentClass+'::');
			var currentText=element.text();
			//console.log(element);
			element.removeClass(currentClass);
			element.html('');
			element.addClass(classes[($.inArray(currentClass, classes)+1)%classes.length]);
			element.html(innerText[($.inArray(currentText, innerText)+1)%innerText.length]);

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
		getMonths: function() {
			var months = new Array(12);
			months[0] = "Jan";
			months[1] = "Feb";
			months[2] = "Mar";
			months[3] = "Apr";
			months[4] = "May";
			months[5] = "Jun";
			months[6] = "Jul";
			months[7] = "Aug";
			months[8] = "Sep";
			months[9] = "Oct";
			months[10] = "Nov";
			months[11] = "Dec";

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
			//console.log('Inside view method');
			rows = `<option value="0" default=true>-None-</option>`;
			for (let i=0, l=data.length; i < l; i++){
				rows = rows+`<option value="`+data[i]+`">`+data[i]+`</option>`;
			}
			$('#year').append(rows);
		},
		showExistingdataForEdit: function(shifts){
			//console.log(shifts);
			let rows = ''
			let dates=this.getDatesInMonth(shifts.month_id-1,shifts.year);
			ns.dates=dates;
			let days = this.getDaysInMonth(shifts.month_id-1,shifts.year);
			ns.days=days;

            // clear the table
            $('.shifts table > tbody').empty();
			rows+=`<tr><td></td><td></td>`;
			for (let i=0, l=days.length; i < l; i++){
				rows = rows+`<th scope="col">`+days[i]+`</th>`;
			}
			rows+=`</tr>`;
			
			rows+=`<tr><td>Resource</td><td>Shift</td>`;
			for (let i=0, l=dates.length; i < l; i++){
				rows = rows+`<th scope="col">`+dates[i]+`</th>`;
			}
			rows+=`</tr>`;
			
            // did we get a people array?
			let userShifts=shifts.usershift;
            if (shifts) {
				for (let i=0, l=userShifts.length; i < l; i++) {
					rows = rows+`<tr class="datarow">`;
					rows+=`<td class="resourcename" id="user" scope="row" ignore="true" name="user_id" val="`+userShifts[i].user_id+`">`+userShifts[i].user_name+`</td>`;
					rows+=`<td class="shiftname" id="shift" name="shift_id" ignore="true" val="`+userShifts[i].shift_id+`">`+userShifts[i].shift_name+`</td>`;
					for (let j=0, k=dates.length; j < k; j++){
						if(Array.isArray(userShifts[i].weekoff) && userShifts[i].weekoff.indexOf(dates[j]) > -1){
							rows=rows + `<td class="weekoff datetoggle" id="datedata">WO</td>`;
						}else if(Array.isArray(userShifts[i].leave) && userShifts[i].leave.indexOf(dates[j]) > -1){
							rows=rows + `<td class="plan-leave datetoggle" id="datedata">PL</td>`;
						}else if(Array.isArray(userShifts[i].unplannedLeave) && userShifts[i].unplannedLeave.indexOf(dates[j]) > -1){
							rows=rows + `<td class="unplan-leave datetoggle" id="datedata">UL</td>`;
						}else{
							rows=rows + `<td class="weekday datetoggle" id="datedata">WD</td>`;
						}
					}
					rows=rows + `</tr>`;
				}

                
            }
            $('.shifts table > tbody').append(rows);
            $('.shifts table > tbody').append(`<button type="button" class="btn btn-primary add-more-rows" id="add-more-rows">Add</button>`);
            //console.log(rows);
            $('.shifts').show();
            
            $('.add-more-rows').on('click',function(e) {
            	ns.view.addBlankRow();
          	});
			$('table').find('td').unbind();
		    $('table').find('td').click(function(e){
		    	if($(this).attr("ignore") == 'false'){
		    		ns.view.rotateClass($(e.target));
		    	}		 	   
		 	});
            
		},
		addBlankRow: function() {
			//console.log('inside adding blank row');
			let rows = `<tr class="datarow">`;
				
			rows+=`<td class="resourcename" ignore="true"><select id="user" name="user_id"><option value="0000" default=true>-None-</option>`;
			for (let i=0, l=ns.users.length; i < l; i++){
				rows = rows+`<option value="`+ns.users[i].userId+`">`+ns.users[i].userName+`</option>`;
			}
			rows+=`</select></td>`;
			//console.log(rows);
			
			rows+=`<td class="shiftname" ignore="true"><select id="shift" name="shift_id"><option value="0000" default=true>-None-</option>`;
			for (let i=0, l=ns.shifts.length; i < l; i++){
				rows = rows+`<option value="`+ns.shifts[i].shift_id+`">`+ns.shifts[i].shift_name+`</option>`;
			}
			rows+=`</select></td>`;
			
			for (let i=0, l=ns.dates.length; i < l; i++){
				rows = rows+`<td class="weekday datetoggle" id="datedata" ignore="false">WD</td>`;
			}
			
			rows+=`</tr>`;
			$('.table-responsive-lg tr:last').after(rows);
			$('table').find('td').unbind();
		    $('table').find('td').click(function(e){
		    	if($(this).attr("ignore") == 'false'){
		    		ns.view.rotateClass($(e.target));
		    	}		 	   
		 	});
			
        },
        update_editor: function(fname, lname) {
            $lname.val(lname);
            $fname.val(fname).focus();
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
	$('.alert').hide();
	$('.shifts').hide();
    let model = m,
        view = v,
        $event_pump = $('body');
    model.getUsers();model.getShifts();

    // Get the data from the model after the controller is done initializing
    setTimeout(function() {
    	//$('.shifts').append('jjhjhjh');
    	view.addMonthDropDown();
    }, 100)


    
    $('.btn-secondary').click(function(e) {
    	//console.log('uploading ...');
    	/*$('.table-responsive-lg tr').each(function() {
    	    console.log('shg sg fj'+$(this));
    	});*/
        var data = $('.table-responsive-lg').map(function() {
        	var shiftdata={};
        	shiftdata['month_id']=$('#month').val();
        	shiftdata['year']=$('#year').val();
        	var userShifts=[]        	
        	$(this).find('.datarow').each(function() {
        		var i=-1;
        		var ignore=false;
        		var obj = {};
        		var weekoff=[];
            	var leave=[];
            	var unplanned=[];
        		$(this).find('td').each(function(){
        			if($(this).attr("class") == 'resourcename' || $(this).attr("class") == 'shiftname'){
        				//console.log($(this).attr("name"));
        				if($(this).attr("name") == 'user_id' || $(this).attr("name") == 'shift_id'){
        					obj[$(this).attr("name")] = $(this).attr("val");
        				}else{
        					if($(this).find('select').val() == "0000"){
        						ignore=true;
        						return false;
        					}
        					obj[$(this).find('select').attr("name")] = $(this).find('select').val();
        				}
        			}else{
        				var day=$(this).attr("class").replace('datetoggle','').trim();
        				switch(day){
        				case 'weekoff':weekoff.push(i);break;
        				case 'plan-leave':leave.push(i);break;
        				case 'unplan-leave':unplanned.push(i);break;
        				}
        			}
        			
        			i=i+1;
        		});
        		if(!ignore){
        			obj["weekoff"] = weekoff;
            		obj["leave"] = leave;
            		obj["unplannedLeave"] = unplanned;
            		userShifts.push(obj);
        		}
        		
            });
        	shiftdata['usershift']=userShifts;
            return shiftdata;
        }).get();
        console.log(JSON.stringify(data[0]));
        model.create(JSON.stringify(data[0]));
    });
    
    $("#month").change(function() {
	  monthSelected=$('#month').val();
	  model.getYears();
	});
    
    $("#year").change(function() {
    	//console.log('Month:'+$('#month').val());
    	//console.log('Year:'+$('#year').val());
    	if($('#month').val() != 0 && $('#year').val() != 0){
    		model.loadExistingData($('#month').val(),$('#year').val());
    	}else{
    			$('.alert').show();
    			$('.message').html("Select Month and Year");
    			setTimeout(function() { // this will automatically close in 5 secs
    			      $(".alert").hide();
    			    }, 5000);
    	}  	  
  	});
    
    /*$('.add-more-rows').on('click',function(e) {
    	console.log('adding new row');
    	view.addBlankRow();
  	});*/


    /*$('#reset').click(function() {
        view.reset();
    })*/
//
//    $('table > tbody').on('dblclick', 'tr', function(e) {
//        let $target = $(e.target),
//            fname,
//            lname;
//
//        fname = $target
//            .parent()
//            .find('td.fname')
//            .text();
//
//        lname = $target
//            .parent()
//            .find('td.lname')
//            .text();
//
//        view.update_editor(fname, lname);
//    });

    // Handle the model events
    $event_pump.on('model_read_success', function(e, data) {
        view.build_table(data);
        //view.reset();
    });
    
    $event_pump.on('model_years_success', function(e, data) {
        view.addYearDropDown(data);
    });
    
    $event_pump.on('model_users_success', function(e, data) {
        view.addUsersDropDown(data);
    });

    $event_pump.on('model_create_success', function(e, data) {
        model.read();
    });

    $event_pump.on('model_update_success', function(e, data) {
    	$('.alert').show();
		$('.message').html("Update successful");
		setTimeout(function() { // this will automatically close in 5 secs
		      $(".alert").hide();
		    }, 5000);
    });

    $event_pump.on('model_delete_success', function(e, data) {
        model.read();
    });
    
    $event_pump.on('model_editload_success', function(e, data) {
        view.showExistingdataForEdit(data);
    });

    $event_pump.on('model_error', function(e, xhr, textStatus, errorThrown) {
        let error_msg = textStatus + ': ' + errorThrown + ' - ' + xhr.responseJSON.detail;
        view.error(error_msg);
        console.log(error_msg);
    })
}(ns.model, ns.view));
