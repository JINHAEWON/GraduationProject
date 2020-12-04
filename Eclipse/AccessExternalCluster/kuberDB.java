package dbtesting;

import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;

import io.kubernetes.client.util.KubeConfig;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1DeploymentBuilder;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.openapi.models.V1ServiceBuilder;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.Yaml;

public class kuberDB {
	
	kuberDB() {
		
	}
	
	public static void main(String args[]) {

	}
	
	public int kubernetes(String id) throws IOException, ApiException{
	 
		
		int database_nodeport = 31111;

    	// 1. container  연결
    	
    	// KubeConfig의 파일 경로
    	// config 파일안에 어디 쿠버네티스로 들어가는지 다 나옴 
        String kubeConfigPath = "C:/kube/config";

        System.out.println(kubeConfigPath);
        
        // 파일 시스템에서 클러스터 외부 구성, kubeconfig로드
        ApiClient client =
            ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();

        
        //전역 기본 api-client를 위의 클러스터 내 클라이언트로 설정합니다.
        Configuration.setDefaultApiClient(client);

        //CoreV1Api는 전역 구성에서 기본 api-client를 로드합니다.
        CoreV1Api api = new CoreV1Api();
       
        //CoreV1Api 클라이언트를 호출합니다.
        V1PodList list =
            api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null);
        for (V1Pod item : list.getItems()) {
          System.out.println(item.getMetadata().getName());
        }
        
		
		
    //1. database pod 추가하기 
      V1Deployment v1Database = 
              new V1DeploymentBuilder()
                  .withApiVersion("apps/v1")
                  .withKind("Deployment")
                  .withNewMetadata().withName(id+"database").endMetadata()
                  .withNewSpec()
                  .withNewSelector()
                  .withMatchLabels(Collections.singletonMap("app",id+"database"))
                  .endSelector()
                  .withReplicas(1)
                  .withNewTemplate()
                  .withNewMetadata().addToLabels("app",id+"database").endMetadata()
                  .withNewSpec()
                  .addNewContainer()
                  .addNewEnv().withName("MYSQL_ROOT_PASSWORD").withValue("root")
                  //.withNewValueFrom().withNewSecretKeyRef()
                  //.withName("root").withKey("password").endSecretKeyRef().endValueFrom()
                  .endEnv()
                  .withName(id+"database")
                  .withImage("mysql")
                  .addNewPort().withContainerPort(3306).endPort()
                  .endContainer()
                  .endSpec()
                  .endTemplate()
                  .endSpec()
                  .build();
      
        System.out.println(Yaml.dump(v1Database));
        AppsV1Api appsV1ApiDatabase = new AppsV1Api(client);
        v1Database = appsV1ApiDatabase.createNamespacedDeployment("default",v1Database,null,null,null);
        
          
        V1Service databasesvc =
                new V1ServiceBuilder()
                    .withApiVersion("v1")
                    .withKind("Service")
                    .withNewMetadata().withName(id+"database").addToLabels("app",id+"database").endMetadata()
                    .withNewSpec()
                    .withType("NodePort")  //여기서 database의 nodeport를 설정하고
                    .addNewPort()
                    .withProtocol("TCP")
                    .withName("http") 
                    .withPort(8888)
                    .withTargetPort(new IntOrString(3306))
                    .endPort()
                    .addToSelector("app",id+"database")
                    .endSpec()
                    .build();
            System.out.println(Yaml.dump(databasesvc));
            databasesvc = api.createNamespacedService("default", databasesvc, null, null, null); 
            System.out.println(databasesvc.getSpec().getPorts().get(0).getNodePort());
            database_nodeport = databasesvc.getSpec().getPorts().get(0).getNodePort();
            
         // svc파일에서 nodeport 값을 읽어오는 것 
        
        return database_nodeport;
	}
}
