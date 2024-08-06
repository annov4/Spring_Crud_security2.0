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
        let result = `
<tr>
<td>${user.id}</td>
<td>${user.age}</td>
<td>${user.name}</td>
<td>${user.email}</td>
<td>${user.address}</td>
<td>${user.role.map(r => r.role.substring(5)).join(', ')}</td>
</tr>
`;

        $('#userTableBody').html(result);
        if (weatherCondition.toLowerCase() === 'rain') {
            $('#umbrellaIcon').html('<i class="bi bi-umbrella"></i>');
        } else {
            $('#umbrellaIcon').html('&nbsp;');
        }
    }

    getUserPage();
});
