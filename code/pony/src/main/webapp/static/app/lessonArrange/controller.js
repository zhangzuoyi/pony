'use strict';

App.controller('LessonArrangeController', ['$scope', 'LessonArrangeService','SubjectService', function($scope, LessonArrangeService, SubjectService) {
              
    $scope.changeClass = function(classId){
    	if(classId != $scope.classId){
    		$scope.classId=classId;
    		$scope.findByClass($scope.classId);
    	}
    	
    };
	$scope.findByClass = function(classId){
    	LessonArrangeService.findByClass(classId).then(
			function(d){
				$scope.arrange=d;
			}
    	);
    };
    
    //$scope.findByClass(2);
	
    $scope.addClass = function($event){
    	$("td.content").removeClass("selected"); 
    	$($event.target).addClass("selected"); 
    };
    
    $scope.editArrange = function($event,weekDayIndex,periodIndex){
    	$scope.weekDayVo=$scope.arrange.weekDays[weekDayIndex];
    	$scope.periodVo=$scope.weekDayVo.periods[periodIndex];
    	$scope.la={};
    	$scope.la.arrangeId=$scope.periodVo.arrangeId;
    	$scope.la.classId=$scope.arrange.classId;
    	//$scope.la.className=$scope.arrange.className;
    	//$scope.la.lessonType=$scope.periodVo.lessonType;
    	$scope.la.otherLesson=$scope.periodVo.otherLesson;
    	$scope.la.weekDay=$scope.weekDayVo.weekDay;
    	//$scope.la.weekDayName=$scope.weekDayVo.weekDayName;
    	$scope.la.lessonPeriod=$scope.periodVo.lessonPeriod;
    	$scope.la.schoolYear=$scope.arrange.schoolYear;
    	$scope.la.term=$scope.arrange.term;
    	$scope.la.subject=$scope.periodVo.subject;
    	$('#my-dialog').dialog('open');
    };
    
    $scope.submitLa = function(){
    	if($scope.la.arrangeId){
    		LessonArrangeService.editArrange($scope.la)
    		.then(function(d){
    			$scope.findByClass($scope.la.classId);
    			$('#my-dialog').dialog('close');
    		});
    	}else{
    		LessonArrangeService.addArrange($scope.la)
    		.then(function(d){
    			$scope.findByClass($scope.la.classId);
    			$('#my-dialog').dialog('close');
    		});
    	}
    };
    
    $scope.deleteLa = function(){
    	if(confirm("确认删除吗？")){
    		LessonArrangeService.deleteArrange($scope.la.arrangeId)
    		.then(function(d){
    			$scope.findByClass($scope.la.classId);
    			$('#my-dialog').dialog('close');
    		});
    	}
    };
    
    $scope.closeDialog = function(){
    	$('#my-dialog').dialog('close');
    };
    SubjectService.findClassSubject()
    	.then(function(d){
    		$scope.subjects=d;
    	});
	/*self.fetchAllUsers = function(){
              UserService.fetchAllUsers()
                  .then(
      					       function(d) {
      						        self.users = d;
      					       },
            					function(errResponse){
            						console.error('Error while fetching Currencies');
            					}
      			       );
          };
           
          self.createUser = function(user){
              UserService.createUser(user)
		              .then(
                      self.fetchAllUsers, 
				              function(errResponse){
					               console.error('Error while creating User.');
				              }	
                  );
          };

         self.updateUser = function(user, id){
              UserService.updateUser(user, id)
		              .then(
				              self.fetchAllUsers, 
				              function(errResponse){
					               console.error('Error while updating User.');
				              }	
                  );
          };

         self.deleteUser = function(id){
              UserService.deleteUser(id)
		              .then(
				              self.fetchAllUsers, 
				              function(errResponse){
					               console.error('Error while deleting User.');
				              }	
                  );
          };

          self.fetchAllUsers();

          self.submit = function() {
              if(self.user.id==null){
                  console.log('Saving New User', self.user);    
                  self.createUser(self.user);
              }else{
                  self.updateUser(self.user, self.user.id);
                  console.log('User updated with id ', self.user.id);
              }
              self.reset();
          };
              
          self.edit = function(id){
              console.log('id to be edited', id);
              for(var i = 0; i < self.users.length; i++){
                  if(self.users[i].id == id) {
                     self.user = angular.copy(self.users[i]);
                     break;
                  }
              }
          };
              
          self.remove = function(id){
              console.log('id to be deleted', id);
              if(self.user.id === id) {//clean form if the user to be deleted is shown there.
                 self.reset();
              }
              self.deleteUser(id);
          };

          
          self.reset = function(){
              self.user={id:null,username:'',address:'',email:''};
              $scope.myForm.$setPristine(); //reset Form
          };*/

      }]);
