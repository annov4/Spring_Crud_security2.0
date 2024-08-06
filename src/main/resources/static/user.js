const userUrl = 'api/user';

$(document).ready(function() {
    function getUserPage() {
        $.ajax({
            url: userUrl,
            method: 'GET',
            dataType: 'json',
            success: function(response) {
                const user = response.user;
                const weatherCondition = response.weatherCondition;
                getInformationAboutUser(user, weatherCondition);
            }
        });
    }

    function getInformationAboutUser(user, weatherCondition) {
        let result = '';

        result = `
                ${user.id}
                ${user.age}
                ${user.name}
                ${user.email}
                ${user.home_address}
                ${user.role.map(r => r.role.substring(5)).join(', ')}
            `

        $('#userTableBody').html(result);

        if (weatherCondition.toLowerCase() === 'rain') {
            $('#umbrellaIcon').html('<i class="bi bi-umbrella"></i>');
        } else {
            $('#umbrellaIcon').html('&nbsp;');
        }
    }

    getUserPage();
});
