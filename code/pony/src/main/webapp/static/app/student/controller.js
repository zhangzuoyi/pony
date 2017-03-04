'use strict';

App.controller('StudentController', ['$scope', 'StudentService', function($scope, StudentService) {
              
    StudentService.mycourse()
    	.then(function(d){
    		$scope.arrange=d;
    	});

      }]);
