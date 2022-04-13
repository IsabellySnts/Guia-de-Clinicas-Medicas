package br.senai.sp.cfp138.clinicguia.util;

import java.io.IOException;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Service
public class FirebaseUtil {

	//variaveis para guardar as credenciais do Firebase
	private Credentials credenciais;
	//varivel para acessar o storager
	private Storage storage;
	//constante para o nome do bucket
	private final String BUCKET_NAME = "clinicguia.appspot.com";
	//constante para o prefixo da URL
	private final String PREFIX = "https://firebasestorage.googleapis.com/v0/b/" +BUCKET_NAME+"/o/";
	//constante para o sufixo da URL
	private final String SUFFIX = "?alt=media";
	//constante para a url
	private final String DOWNLOAD_URL= PREFIX +"%s"+SUFFIX;
	
	public FirebaseUtil() {
		//buscar as credenciais(arquivo JSON)
		Resource resource = new ClassPathResource("chavefirebase.json");
	
		try {
			//ler o arquivo para obter as credenciais
			credenciais = GoogleCredentials.fromStream(resource.getInputStream());
			//acessa o serviço de storage
			storage = StorageOptions.newBuilder().setCredentials(credenciais).build().getService();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	
	//metodo do upload
		public String uploadFile(MultipartFile arquivo) throws IOException {
			
			//gera uma strins aleatoria para o nome do arquivo
			String nomeArquivo = UUID.randomUUID().toString() + getExtensao(arquivo.getOriginalFilename());
			
			//criar BlobId
			BlobId blobId = BlobId.of(BUCKET_NAME, nomeArquivo);
			
			//criando um blobInfo a partir do BlobId
			BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
			
			//mandando o BlobInfo para o Storage passando os byte do arquivo pra ele
			storage.create(blobInfo, arquivo.getBytes());
			
			//retornando a URL para acessar o arquivo
			return String.format(DOWNLOAD_URL, nomeArquivo);
		}
	
	//metodo que retorna a extensao de um arquivo atraves do seu nome
	private String getExtensao(String nomeArquivo) {
		
		//retorna a parte da String dps no ultimo ponto
		
	return nomeArquivo.substring(nomeArquivo.lastIndexOf('.'));
	}
	
	//metodo para excluir a foto do Firebase
	public void deletar(String nomeArquivo) {
		
		//retirando o prefixo e sufixo do nome do arquivo
		
		nomeArquivo = nomeArquivo.replace(PREFIX, "").replace(SUFFIX, "");
		
		//pegando o Blob através do nome do arquivo
		
		Blob blob = storage.get(BlobId.of(BUCKET_NAME, nomeArquivo));
		
		//deletando o arquido
		
		storage.delete(blob.getBlobId());
	}
	
	
}
