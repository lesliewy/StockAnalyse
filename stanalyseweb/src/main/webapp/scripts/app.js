// 依赖ui.bootstrap模块
capFlow = angular.module('capFlow', ['ui.bootstrap']);

// 获取历史资金流向service.
capFlow.factory('getCapFlowService', function($http) {
	var runCapRequest = function(code, tradeDate){
   return	$http({
					method: 'GET',
					url: '/stanalyse/capFlow/stockCap',
					params: {
						'code': code,
                  'tradeDate': tradeDate
					}
				})
/*
	.success(function(data, status, headers, config){
					// 处理成功的响应
					return data;
				}).error(function(data, status, headers, config){
					// 处理非成功的响应
					return status;
				});
*/
	};
	return {
		events: function(code, tradeDate){
			return runCapRequest(code, tradeDate);
		}
	};
});

capFlow.controller('capHistController', function($scope, getCapFlowService){
	$scope.allCapsQuery = function() {
	   var promise = getCapFlowService.events($scope.code, $scope.tradeDate);
		promise.success(function(data){
         $scope.allCaps = data;
		})
		console.log("allCaps: " + $scope.allCaps);
		console.log("tradeDate:" + $scope.tradeDate);
	}
});

// 日期控件controller
capFlow.controller('dateControl', function($scope){
	$scope.open = function($event){ 
	   $event.preventDefault();
	   $event.stopPropagation();
	   $scope.opened = true;
	};
   $scope.today = function(){
      $scope.tradeDate = new Date();
   };
   $scope.today(); 
	// 设置option,  将星期一排在第一列.
	$scope.dateOptions = {
	   startingDay : 1
	}
	// 设置格式
	$scope.format = 'yyyy-MM-dd 15:00:00';
	// disable 掉周末
	$scope.disabled = function(date , mode){ 
	   return (mode === 'day' && (date.getDay() === 0 || date.getDay() === 6))
	}
});
