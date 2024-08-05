const userUrl = 'api/user';

$(document).ready(function() {
    function getUserPage() {
        $.ajax({
            url: userUrl,
            method: 'GET',
            dataType: 'json',
            success: function(user) {
                getInformationAboutUser(user);
                getWeatherInfo(user.home_address);
            }
        });
    }
    function getWeatherInfo(address) {
        $.ajax({
            url: `${weatherUrl}?address=${encodeURIComponent(address)}`,
            method: 'GET',
            dataType: 'json',
            success: function(weather) {
                appendWeatherIcon(weather.condition);
            }
        });
    }

    function appendWeatherIcon(condition) {
        if (condition === 'RAIN') {
            $('#homeAddress').append('<i class="bi bi-umbrella"></i>');
        }
    }

    function getInformationAboutUser(user) {
        let result = `
    ${user.id}
    ${user.age}
    ${user.name}
    ${user.email}
    <span id="homeAddress">${user.home_address}</span>
    ${user.role.map(r => r.role.substring(5)).join(', ')}
`
        $('#userTableBody').html(result);
    }

    getUserPage();
});