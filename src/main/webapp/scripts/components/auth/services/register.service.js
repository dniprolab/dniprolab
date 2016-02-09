'use strict';

angular.module('dniprolabApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


