/**
 * Created by Overlord on 01.04.2016.
 */

'use strict';

angular.module('dniprolabApp')
    .factory('Video', function($resource, DateUtils){
        return $resource('api/video/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function(data){
                    data = angular.fromJson(data);
                    data.created = DateUtils.convertDateTimeFromServer(date.created);
                    return data;
                },
                'update': { method:'PUT' }
            }
        });
    });
