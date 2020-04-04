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
        getExceptions: function() {
        	let ajax_options = {
                type: 'GET',
                url: '/static/exceptions'
            };
            $.ajax(ajax_options)
            .done(function(data) {
                ns.exceptions=data;
                for (let i=0, l=ns.exceptions.length; i < l; i++){
    				ns.classes.push(ns.exceptions[i].excp_name);
    			}
            })
            .fail(function(xhr, textStatus, errorThrown) {
                $event_pump.trigger('model_error', [xhr, textStatus, errorThrown]);
            })			
			
        },
        loadVariance: function(month,year) {
        	let ajax_options = {
                    type: 'GET',
                    url: '/shift/att_variance/'+month+'/'+year,
                };
                $.ajax(ajax_options)
                .done(function(data) {
                    $event_pump.trigger('model_variance_success', [data]);
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
                    //$event_pump.trigger('model_generate_success', [data]);
                })
                .fail(function(xhr, textStatus, errorThrown) {
                    $event_pump.trigger('model_error', [xhr, textStatus, errorThrown]);
                })			
    			
            },
        create: function(data) {
            let ajax_options = {
                type: 'POST',
                url: '/shift/update/actual',
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
			var currentClass=element.attr("class").replace('datetoggle','').trim();
			var currentText=element.text();
			element.removeClass(currentClass);
			element.html('');
			element.addClass(ns.classes[($.inArray(currentClass, ns.classes)+1)%ns.classes.length]);
			element.html(ns.classes[($.inArray(currentText, ns.classes)+1)%ns.classes.length]);

		},
		getDaysInMonth: function(month, year) {
			var weekday = ["Su","Mo","Tu","We","Th","Fr","Sa"];
			
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
		isActual: function(type) {
			  return (type === 'A' ? 'Actual' : 'Planned');
			},
		getName: function(type,name) {
			  return (type === 1 ? name : '');
			},
		showVarianceData: function(shifts){
			let rows = ''
			let dates=this.getDatesInMonth($('#month').val()-1,$('#year').val());
			ns.dates=dates;
			let days = this.getDaysInMonth($('#month').val()-1,$('#year').val());
			ns.days=days;

            // clear the table
            $('.shifts table > tbody').empty();
			
			 rows+=`<tr class="head"><td></td>`;
			for (let i=0, l=days.length; i < l; i++){
				rows += `<th scope="col">`+days[i]+`</th>`;
			}
			rows+=`</tr>`;
			
			rows+=`<tr class="head"><td></td>`;
			for (let i=0, l=dates.length; i < l; i++){
				rows+=`<th scope="col">`+dates[i]+`</th>`;
			}
			rows+=`</tr>`;
			
            // did we get a people array?
			//console.log(shifts);
            if (shifts) {
            	let type = 1;
				for (let i=0, l=shifts.length; i < l; i++) {
					/*if(type === 1){
						rows+=`<tr><td></td><td></td>`;
						for (let i=0, l=dates.length; i < l; i++){
							rows = rows+`<th scope="col">`+dates[i]+`</th>`;
						}
						rows+=`</tr>`;
						
					}*/
					rows = rows+`<tr class="datarow">`;
					rows+=`<td class="text-nowrap">`+shifts[i].user_name+`</td>`;
					
					//rows+=`<td>`+this.isActual(userShifts[i].typeOfData)+`</td>`;
					//console.log(userShifts[i].typeOfData === 'A'?'Actual':'Planned');
					type+=1;
					let colVal='';
					for (let j=0, k=dates.length; j < k; j++){
						colVal='';
						for (let s=0; s < shifts[i].timeDeficitList.length;s++){
							if(shifts[i].timeDeficitList[s].date == dates[j]){
								colVal=shifts[i].timeDeficitList[s].timeDeficit;
							}
						}
								rows += `<td class="WD"><small>`+colVal+`</small></td>`;
						}
						
					rows += `</tr>`;
				}

                
            }
            $('.shifts table > tbody').append(rows);
            //$('.shifts table > tbody').append(`<button type="button" class="btn btn-primary add-more-rows" id="add-more-rows">Add</button>`);
            
            $('.shifts').show();
            
            /*$('.add-more-rows').on('click',function(e) {
            	ns.view.addBlankRow();
          	});
			$('table').find('td').unbind();
		    $('table').find('td').click(function(e){
		    	if($(this).attr("ignore") == 'false'){
		    		ns.view.rotateClass($(e.target));
		    	}
		 	});
		 	*/
            
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
	$('.error').hide();

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
    		model.loadVariance($('#month').val(),$('#year').val());

    	}else{
    			$('.alert').show();
    			$('.message').html("Select Month and Year");
    			setTimeout(function() { // this will automatically close in 5 secs
    			      $(".alert").hide();
    			    }, 5000);
    	}  	  
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
    
    $event_pump.on('model_variance_success', function(e, data) {
        view.showVarianceData(data);
    });

    $event_pump.on('model_error', function(e, xhr, textStatus, errorThrown) {
        let error_msg = textStatus + ': ' + errorThrown + ' - ' + xhr.responseJSON.detail;
        view.error(error_msg);
        console.log(error_msg);
    })
}(ns.model, ns.view));
