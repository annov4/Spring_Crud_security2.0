const adminUrl = 'api/admin';

function loadTable(listAllUsers) {
    let res = '';
    for (let user of listAllUsers) {
        res  +=  `<tr>
    <td>${user.id}</td>
    <td>${user.age}</td>
    <td>${user.name}</td>
    <td>${user.email}</td>
    <td>${user.homeAddress}</td>
    <td id=${'role' + user.id}>${user.role.map(r => r.role.substring(5)).join(', ')}</td>
                <td><button class="btn btn-info" type="button"
                 data-bs-toggle="modal" data-bs-target="#editModal"
                 onclick="editModal(${user.id})">Edit</button></td>
                <td><button class="btn btn-danger" type="button"
                data-bs-toggle="modal" data-bs-target="#deleteModal"
                onclick="deleteModal(${user.id})">Delete</button></td>
         </tr>            
`}
    $('#tableBodyAdmin').html(res);
}

function getAllUsers() {
    $.ajax({
        url: adminUrl,
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            loadTable(data);
        }
    });
}

$(document).ready(function () {
    getAllUsers();

    $('#newUserForm').on('submit', function (e) {
        e.preventDefault();
        let role = $('#role_select');
        let rolesAddUser = [];
        role.find('option:selected').each(function () {
            rolesAddUser.push({id: $(this).val(), name: 'ROLE_' + $(this).text()});
        });

        $.ajax({
            url: adminUrl,
            method: 'POST',
            contentType: 'application/json;charset=utf-8',
            data: JSON.stringify({
                age: $('#newAge').val(),
                name: $('#newName').val(),
                email: $('#newEmail').val(),
                home_address: $('#newHomeAddress').val(),
                password: $('#newPassword').val(),
                role: rolesAddUser
            }),
            success: function () {
                getAllUsers();
                $('#all-users-tab').click();
            }
        });
    });
});

function editModal(id) {
    $.ajax({
        url: adminUrl + '/' + id,
        method: 'GET',
        dataType: 'json',
        success: function (u) {
            $('#editId').val(u.id);
            $('#editAge').val(u.age);
            $('#editName').val(u.name);
            $('#editEmail').val(u.email);
            $('#editHomeAddress').val(u.homeAddress);
            $('#editPassword').val('');
            $('#currentPassword').val(u.password);
        }
    });
}

function editUser() {
    let idValue = $('#editId').val();
    let ageValue = $('#editAge').val();
    let nameValue = $('#editName').val();
    let emailValue = $('#editEmail').val();
    let home_addressValue = $('#editHomeAddress').val();
    let passwordValue = $('#editPassword').val();
    let currentPassword = $('#currentPassword').val();
    let passwordToUpdate = passwordValue === '' ? currentPassword : passwordValue;

    let listOfRole = [];
    $('#editRole option:selected').each(function () {
        listOfRole.push({id: $(this).val(), name: 'ROLE_' + $(this).text()});
    });
    let user = {
        id: idValue,
        age: ageValue,
        name: nameValue,
        email: emailValue,
        home_address: home_addressValue,
        password: passwordToUpdate,
        role: listOfRole
    };

    $.ajax({
        url: adminUrl + '/' + user.id,
        method: 'PUT',
        contentType: 'application/json;charset=utf-8',
        data: JSON.stringify(user),
        success: function () {
            closeModal();
            getAllUsers();
        }
    });
}

function deleteModal(id) {
    $.ajax({
        url: adminUrl + '/' + id,
        method: 'GET',
        dataType: 'json',
        success: function (u) {
            $('#deleteId').val(u.id);
            $('#deleteAge').val(u.age);
            $('#deleteName').val(u.name);
            $('#deleteEmail').val(u.email);
            $('#deleteHomeAddress').val(u.homeAddress);
            $('#deleteRole').val(u.role.map(r => r.role.substring(5)).join(", "));
        }
    });
}

function deleteUser() {
    const id = $('#deleteId').val();
    $.ajax({
        url: url + '/' + id,
        method: 'DELETE',
        success: function () {
            closeModal();
            getAllUsers();
        }
    });
}

function closeModal() {
    $(".btn-close").click();
}