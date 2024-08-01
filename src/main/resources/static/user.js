const userUrl = 'api/user';

$(document).ready(function() {
    function getUserPage() {
        $.ajax({
            url: userUrl,
            method: 'GET',
            dataType: 'json',
            success: function(user) {
                getInformationAboutUser(user);
            }
        });
    }
function getInformationAboutUser(user) {

    let result = '';
    result =

        `<tr>
    <td>${user.id}</td>
    <td>${user.age}</td>
    <td>${user.name}</td>
    <td>${user.email}</td>
    <td>${user.home_address}</td>
    <td id=${'role' + user.id}>${user.role.map(r => r.role.substring(5)).join(', ')}</td>
</tr>`
    $('#userTableBody').html(result);
}

    getUserPage();
});