 'use strict';

angular.module('dniprolabApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-dniprolabApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-dniprolabApp-params')});
                }
                return response;
            }
        };
    });
