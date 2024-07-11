const url = 'http://localhost:8080/api/admin';

function getUserData() {
    $.ajax({
        url: url,
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            loadTable(data);
        }
    });
}

function getAllUsers() {
    $.ajax({
        url: url,
        method: 'GET',
        dataType: 'json',
        success: function (user) {
            loadTable(user);
        }
    });
}

function loadTable(listAllUsers) {
    let res = '';
    for (let user of listAllUsers) {
        res += `
            <tr>
                <td>${user.id}</td>
                <td>${user.age}</td>
                <td>${user.email}</td>
                <td>${user.name}</td>
                <td>${user.role.map(r => r.role.substring(5)).join(', ')}</td>
                <td><button class="btn btn-info" onclick="editModal(${user.id})" data-bs-toggle="modal" data-bs-target="#editUserModal">Edit</button></td>
                <td><button class="btn btn-danger" onclick="deleteModal(${user.id})" data-bs-toggle="modal" data-bs-target="#deleteUserModal">Delete</button></td>
            </tr>
        `;
    }
    $('#tableBodyAdmin').html(res);
}

getAllUsers();

// Новый юзер
$('#newUserForm').on('submit', function (e) {
    e.preventDefault();
    let role = $('#role_select');
    let rolesAddUser = [];
    for (let i = 0; i < role[0].options.length; i++) {
        if (role[0].options[i].selected) {
            rolesAddUser.push({id: role[0].options[i].value, name: 'ROLE_' + role[0].options[i].innerHTML});
        }
    }
    $.ajax({
        url: url,
        method: 'POST',
        contentType: 'application/json;charset=utf-8',
        data: JSON.stringify({
            age: $('#inputAge').val(),
            email: $('#inputEmail').val(),
            name: $('#inputName').val(),
            password: $('#inputPassword').val(),
            role: rolesAddUser
        }),
        success: function () {
            getUserData();
            $("#all-users-tab").click();
        }
    });
});

// Изменение юзера
function editModal(id) {
    $.ajax({
        url: url + '/' + id,
        method: 'GET',
        contentType: 'application/json;charset=UTF-8',
        success: function (u) {
            $('#ID').val(u.id);
            $('#Age').val(u.age);
            $('#Email').val(u.email);
            $('#Name').val(u.name);
            $('#Password').val("****");

            let roleElements = $('#Roles').get(0).options;
            for (let i = 0; i < roleElements.length; i++) {
                roleElements[i].selected = u.roles.some(role => 'ROLE_' + roleElements[i].text === role.name);
            }
        }
    });
}

async function editUser() {
    let idValue = $("#ID").val();
    let ageValue = $('#Age').val();
    let emailValue = $('#Email').val();
    let nameValue = $('#Name').val();
    let passwordValue = $("#Password").val();

    let roleElements = $('#Roles').get(0).options;
    let roles = [];
    for (let i = 0; i < roleElements.length; i++) {
        if (roleElements[i].selected) {
            roles.push({
                id: roleElements[i].value,
                name: 'ROLE_' + roleElements[i].text
            });
        }
    }

    let user = {
        id: idValue,
        age: ageValue,
        email: emailValue,
        name: nameValue,
        password: passwordValue,
        roles: roles,
    };

    await $.ajax({
        url: url + '/' + user.id,
        method: 'PUT',
        contentType: 'application/json;charset=UTF-8',
        data: JSON.stringify(user),
        success: function () {
            closeModal();
            getUserData();
        }
    });
}

// Удаление юзера
function deleteModal(id) {
    $.ajax({
        url: url + '/' + id,
        method: 'GET',
        contentType: 'application/json;charset=UTF-8',
        success: function (u) {
            $('#deleteId').val(u.id);
            $('#deleteAge').val(u.age);
            $('#deleteEmail').val(u.email);
            $('#deleteName').val(u.name);
            $('#deletePassword').val(u.password);
            $('#deleteRoles').val(u.roles.map(r => r.role.substring(5)).join(", ")); // Изменил поле на множественное число roles
        }
    });
}

async function deleteUser() {
    const id = $("#deleteId").val();
    let urlDel = url + "/" + id;

    await $.ajax({
        url: urlDel,
        method: 'DELETE',
        contentType: 'application/json',
        success: function () {
            closeModal();
            getUserData();
        }
    });
}

function closeModal() {
    $(".btn-close").click();
}