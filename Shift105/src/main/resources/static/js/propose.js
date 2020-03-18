/*
 * JavaScript file for the application to demonstrate
 * using the API
 */

// Create the namespace instance
let ns = {};
ns.eventBound=0;
ns.classes=[];

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
            })
            .fail(function(xhr, textStatus, errorThrown) {
                $event_pump.trigger('model_error', [xhr, textStatus, errorThrown]);
            })			
			
        },
        getShiftMonthYearUser: function(month,year,user) {
        	let ajax_options = {
                type: 'GET',
                url: '/shift/'+year+'/'+month+'/'+user,
            };
            $.ajax(ajax_options)
            .done(function(data) {
                ns.monthdata=data;
                $event_pump.trigger('model_proposal_success', data);
            })
            .fail(function(xhr, textStatus, errorThrown) {
                $event_pump.trigger('model_error', [xhr, textStatus, errorThrown]);
            })
			
        },
        getExceptions: function(prop) {
        	let ajax_options = {
                type: 'GET',
                url: '/static/exceptions'
            };
            $.ajax(ajax_options)
            .done(function(data) {
                ns.exceptions=data;
                for (let i=0, l=ns.exceptions.length; i < l; i++){
                	if(ns.exceptions[i].excp_name === 'UL')continue;
    				ns.classes.push(ns.exceptions[i].excp_name);
    			}
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
        generateExcel: function(month,year) {
        	let ajax_options = {
                    type: 'GET',
                    url: '/generate/'+month+'/'+year,
                };
                $.ajax(ajax_options)
                .done(function(data) {
                    // $event_pump.trigger('model_generate_success', [data]);
                })
                .fail(function(xhr, textStatus, errorThrown) {
                    $event_pump.trigger('model_error', [xhr, textStatus, errorThrown]);
                })			
    			
            },
        propose: function(data) {
            let ajax_options = {
                type: 'POST',
                url: '/shift/update',
                /* accepts: 'application/json', */
                // dataType: "json",
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
        }
    };
}());

// Create the view instance
ns.view = (function() {
    'use strict';


    // return the API
    return {
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
			var currentClass=element.attr("class");
			var currentText=element.text();
			element.removeClass(currentClass);
			element.addClass(ns.classes[($.inArray(currentClass, ns.classes)+1)%ns.classes.length]);

		},
		getDaysInMonth: function(month, year) {
			var weekday = ["Sun","Mon","Tue","Wed","Thu","Fri","Sat"];
			
		  var date = new Date(year, month, 1);
		  var days = [];
		  while (date.getMonth() === month) {
			days.push(weekday[date.getDay()]);
			date.setDate(date.getDate() + 1);
		  }
		  return days;
		},
		getMonths: function() {
			var months=["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"];

		  return months;
		},
		addMonthDropDown: function(){
			let months=this.getMonths();
			let rows = '';
			$('#month').empty();

			rows = `<option value="0" default=true>-None-</option>`;
			for (let i=0, l=months.length; i < l; i++){
				rows = rows+`<option value="`+(i+1)+`">`+months[i]+`</option>`;
			}
			$('#month').append(rows);
		},
		addYearDropDown: function(data){
			let rows = '';
			$('#year').empty();
			rows = `<option value="0" default=true>-None-</option>`;
			for (let i=0, l=data.length; i < l; i++){
				rows = rows+`<option value="`+data[i]+`">`+data[i]+`</option>`;
			}
			$('#year').append(rows);
		},
		getLastDayofMonth: function(currentMonth) {

			var lastDay = new Date();
			// Set the month to next month
			lastDay.setMonth( currentMonth + 1 );
			// Set the date to one day before the start of the month
			lastDay.setDate(0);

			return lastDay.getDate();

		},
		getMonthText: function(month){
			let months = this.getMonths();
			for(let i=0;i<months.length;i++){
				if(i == month) return months[i];
			}
		},
		calendar: function(y, m) {
		    var firstDay = new Date(y, m, 1); // date object with first day of current month
		    var lastDayofmonth = this.getLastDayofMonth(m); // date object with last day of current month
		    var html = "<tr><th>Sun</th><th>Mon</th><th>Tue</th><th>Wed</th><th>Thu</th><th>Fri</th><th>Sat</th></tr>";
		    var number = 0;
		    for (var i = 0; i < 6; i++) {
		        // creates a table row
		        html += "<tr class='datarow'>";
		        for (let j = 0; j < 7; j++) {
		            if (i === 0 && j < firstDay.getDay()) {
		                html += "<td ignore='true'></td>";
		            } else if (number >= lastDayofmonth) {
		                html += "<td ignore='true'></td>";
		            } else {
		                number += 1;
		                html += "<td ignore='false' class='"+ns.monthdata.usershift[0].excelData[number-1].excp_code+"'>" + number + "</td>";
		            }
		        }

		        html += "</tr>";
		        if (number >= lastDayofmonth) {
		            break;
		        }
		    }
		    $("#calendar").html(html); // writes current month calendar in html table with id calendar
		    $("#month_year").text(this.getMonthText(m) + " " + y); // writes current month and year on html inside month_year span tag
		    
		    $('.shifts').show();
		    
		    $('table').find('td').click(function(e){
		    	if($(this).attr("ignore") == 'false'){
		    		ns.view.rotateClass($(e.target));
		    	}
		 	});
		},

		showExistingdataForEdit: function(shifts){
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
					rows+=`<td class="resourcename"  ignore="true"><select id="user" name="user_id" disabled><option value="0000">-None-</option>`;
					for (let j=0, l=ns.users.length; j < l; j++){
						rows = rows+`<option value="`+ns.users[j].userId+`"`;
						if(userShifts[i].user_id == ns.users[j].userId){
							rows+=` selected="true"`;
						}
						rows+=`>`+ns.users[j].userName+`</option>`;
					}
					rows+=`</select></td>`;
					rows+=`<td class="shiftname" ignore="true"><select id="shift" name="shift_id"><option value="0000">-None-</option>`;
					for (let j=0, l=ns.shifts.length; j < l; j++){
						rows = rows+`<option value="`+ns.shifts[j].shift_id+`"`;
						if(userShifts[i].shift_id == ns.shifts[j].shift_id){
							rows+=` selected="true"`;
						}
						rows+=`>`+ns.shifts[j].shift_name+`</option>`;
					}
					rows+=`</select></td>`;
					// console.log('Value of option'+userShifts[i].shift_id);
					
					for (let j=0, k=dates.length; j < k; j++){
						var out=false;
						$.each(userShifts[i].exceptionData,function(key,value){							
							if(value.dates.indexOf(dates[j]) > -1){
								rows=rows + `<td class="`+value.excp_name+` datetoggle" data_id="`+value.data_Id+`" id="datedata" ignore="false">`+value.excp_name+`</td>`;
								out=true;
								return false;
							}							
						});
						if(out == false)rows=rows + `<td class="WD datetoggle" id="datedata" ignore="false">WD</td>`;
												
					}
					rows=rows + `</tr>`;
				}

                
            }
            $('.shifts table > tbody').append(rows);
            $('.shifts table > tbody').append(`<button type="button" class="btn btn-primary add-more-rows" id="add-more-rows">Add</button>`);
            
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
			let rows = `<tr class="datarow">`;
				
			rows+=`<td class="resourcename" ignore="true"><select id="user" name="user_id"><option value="0000" default=true>-None-</option>`;
			for (let i=0, l=ns.users.length; i < l; i++){
				rows = rows+`<option value="`+ns.users[i].userId+`">`+ns.users[i].userName+`</option>`;
			}
			rows+=`</select></td>`;
			
			rows+=`<td class="shiftname" ignore="true"><select id="shift" name="shift_id"><option value="0000" default=true>-None-</option>`;
			for (let i=0, l=ns.shifts.length; i < l; i++){
				rows = rows+`<option value="`+ns.shifts[i].shift_id+`">`+ns.shifts[i].shift_name+`</option>`;
			}
			rows+=`</select></td>`;
			
			for (let i=0, l=ns.dates.length; i < l; i++){
				rows = rows+`<td class="WD datetoggle" id="datedata" ignore="false">WD</td>`;
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
	$('.btn-update').hide();
	$('.btn-submit').hide();
    let model = m,
        view = v,
        $event_pump = $('body');
    model.getUsers();
    model.getShifts();
    model.getExceptions();

    // Get the data from the model after the controller is done initializing
    setTimeout(function() {
    	view.addMonthDropDown();
    }, 100);
    
    
    $("#month").change(function() {
	  model.getYears();
	});
    
    $("#year").change(function() {
    	if($('#month').val() != 0 && $('#year').val() != 0){
    		model.getShiftMonthYearUser($('#year').val(),$('#month').val(),0);
    		//view.calendar($('#year').val(),$('#month').val()-1);
    		$('.btn-update').show();
    		$('.btn-submit').show();
    	}else{
    			$('.alert').show();
    			$('.message').html("Select Month and Year");
    			setTimeout(function() { // this will automatically close in 5
										// secs
    			      $(".alert").hide();
    			    }, 5000);
    	}  	  
  	});
    
    $('.btn-generate').click(function(e) {
    	// model.generateExcel($('#month').val(),$('#year').val());
    	window.location.href = '/generate/'+$('#month').val()+'/'+$('#year').val();
    });
    
    $('.btn-update').click(function(e) {
        var data = $('.calendar').map(function() {
        	var shiftdata={};
        	shiftdata['month_id']=$('#month').val();
        	shiftdata['year']=$('#year').val();
        	var userShifts=[]        	
        	$(this).find('.datarow').each(function() {
        		var i=1;
        		var ignore=false;
        		var obj= {},weekoffObj= {},leaveObj= {},speLeaveObj = {};
        		var weekoff=[],leave=[],specialLeave=[],exceptionData=[];
        		$(this).find('td').each(function(){
        			if($(this).has( "select" ).length == 1){
    					obj[$(this).find('select').attr("name")] = $(this).find('select').val();
        			}else{
        				var day=$(this).attr("class").replace('datetoggle','').trim();
        				switch(day){
        				case 'WO':
        					weekoff.push(i);
        					weekoffObj["data_Id"]=$(this).attr("data_id");
        					weekoffObj["excp_id"]="0";
        					weekoffObj["dates"]=weekoff;
        					break;
        				case 'PL':
        					leave.push(i);
        					leaveObj["data_Id"]=$(this).attr("data_id");
        					leaveObj["excp_id"]="1";
        					leaveObj["dates"]=leave;
        					break;
        				case 'SL':
        					specialLeave.push(i);
        					speLeaveObj["data_Id"]=$(this).attr("data_id");
        					speLeaveObj["excp_id"]="3";
        					speLeaveObj["dates"]=specialLeave;
        					break;
        				}
        				
        				i+=1;
        			}
        			
        		});
        		if(!$.isEmptyObject(weekoffObj))exceptionData.push(weekoffObj);
        		if(!$.isEmptyObject(leaveObj))exceptionData.push(leaveObj);
        		if(!$.isEmptyObject(speLeaveObj))exceptionData.push(speLeaveObj);

        		obj["exceptionData"]=exceptionData;
        		userShifts.push(obj);
        		
            });
        	shiftdata['usershift']=userShifts;
            return shiftdata;
        }).get();
        // console.log(JSON.stringify(data[0]));
        model.propose(JSON.stringify(data[0]));
    });

    // Handle the model events
    $event_pump.on('model_read_success', function(e, data) {
        view.build_table(data);
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
		setTimeout(function() { // this will automatically close in 1 secs
		      $(".alert").hide();
		    }, 1000);
		model.loadExistingData($('#month').val(),$('#year').val());
    });

    $event_pump.on('model_delete_success', function(e, data) {
        model.read();
    });
    
    $event_pump.on('model_editload_success', function(e, data) {
        view.showExistingdataForEdit(data);
    });

    $event_pump.on('model_proposal_success', function(e, data) {
        view.calendar(data.year,data.month_id-1);
    });
    
    $event_pump.on('model_error', function(e, xhr, textStatus, errorThrown) {
        let error_msg = textStatus + ': ' + errorThrown + ' - ' + xhr.responseJSON.detail;
        view.error(error_msg);
        console.log(error_msg);
    })
}(ns.model, ns.view));
