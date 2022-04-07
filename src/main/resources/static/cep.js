const options = {
    method: "GET",
    mode: "cors",
    caches: "default"
}

const cep = document.getElementById("cep")
cep.addEventListener("blur", (e) => {
    let Cep = document.getElementById("cep").value;
    console.log(Cep)
    let search = cep.value.replace("-", "")
    fetch(`https://viacep.com.br/ws/${search}/json/`, options).then((response) => {
        response.json().then(data => {
            console.log(data)
            document.getElementById("bairro").value = data.bairro;
            document.getElementById("cidade").value = data.localidade;
            document.getElementById("logradouro").value = data.logradouro;
            document.getElementById("uf").value = data.uf;
          

        })
    })
})

function enviar() {
    let bairro = document.getElementById("bairro").value;
    
    let ibge = document.getElementById("ibge").value;
    let cidade = document.getElementById("cidade").value;
    let logradouro = document.getElementById("logradouro").value;
    let uf = document.getElementById("uf").value;
   
    let json = {
        "bairro": bairro,
        "ddd": ddd,
        "ibge": ibge,
        "cidade": cidade,
        "logradouro": logradouro,
        "uf": uf,
      
    }
    console.log(json)
}