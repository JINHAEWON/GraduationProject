package dbtesting;

import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.ApiResponse;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.NetworkingV1beta1Ingress;
import io.kubernetes.client.openapi.models.NetworkingV1beta1IngressBuilder;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1DeploymentBuilder;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.openapi.models.V1ServiceBuilder;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import io.kubernetes.client.util.Yaml;

import java.io.FileReader;
import  java.io.IOException ;
import java.util.Collections;

import io.kubernetes.client.openapi.apis.NetworkingV1beta1Api;


public class kuber {
	kuber() {
		
	}
	
	//private static kuber instance = new kuber();
   // public static kuber getInstance() {
   //     return instance;
   // }
	
    public static void main(String args[]) {
    	
    }
    
    public String kubernetes(String id,String node) throws IOException, ApiException{
    	int port = 8111;
    	String last =null;
    	 
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
        
       
        
   
     // 2. deployment. yaml 파일 작성하기
       
        // 2-1) login - deployment 
        V1Deployment v1Deployment = 
                new V1DeploymentBuilder()
                    .withApiVersion("apps/v1")
                    .withKind("Deployment")
                    .withNewMetadata().withName(id+"login").endMetadata()
                    .withNewSpec()
                    .withNewSelector()
                    .withMatchLabels(Collections.singletonMap("app",id+"login"))
                    .endSelector()
                    .withReplicas(1)
                    .withNewTemplate()
                    .withNewMetadata().addToLabels("app",id+"login").endMetadata()
                    .withNewSpec()
                    .addNewContainer()
                    .withName(id+"login")
                    .withImage("godnjs0415/finallogin2:latest")
                    .addNewPort().withContainerPort(8080).endPort()
                    .addNewEnv().withName("changefile").withNewValue(id+"login").endEnv()
                    .addNewEnv().withName("nodeport").withNewValue(node).endEnv()
                    .endContainer()
                    .endSpec()
                    .endTemplate()
                    .endSpec()
                    .build();
        
          System.out.println(Yaml.dump(v1Deployment));
          AppsV1Api appsV1Api = new AppsV1Api(client);
          v1Deployment = appsV1Api.createNamespacedDeployment("default",v1Deployment,null,null,null);
          
          // 2-2) shop - deployment 
          V1Deployment v1Deployment2 = 
                  new V1DeploymentBuilder()
                      .withApiVersion("apps/v1")
                      .withKind("Deployment")
                      .withNewMetadata().withName(id+"shop").endMetadata()
                      .withNewSpec()
                      .withNewSelector()
                      .withMatchLabels(Collections.singletonMap("app",id+"shop"))
                      .endSelector()
                      .withReplicas(1)
                      .withNewTemplate()
                      .withNewMetadata().addToLabels("app",id+"shop").endMetadata()
                      .withNewSpec()
                      .addNewContainer()
                      .withName(id+"shop")
                      .withImage("godnjs0415/finalshop2:latest")
                      .addNewPort().withContainerPort(8080).endPort()
                      .addNewEnv().withName("changefile").withNewValue(id+"shop").endEnv()
                      .addNewEnv().withName("nodeport").withNewValue(node).endEnv()
                      .endContainer()
                      .endSpec()
                      .endTemplate()
                      .endSpec()
                      .build();
                      
             //System.out.println(Yaml.dump(v1Deployment2));
            AppsV1Api appsV1Api2 = new AppsV1Api(client);
            v1Deployment2 = appsV1Api2.createNamespacedDeployment("default",v1Deployment2,null,null,null);
          
            
            // 2-3) pay - deployment 
            V1Deployment v1Deployment3 = 
                    new V1DeploymentBuilder()
                        .withApiVersion("apps/v1")
                        .withKind("Deployment")
                        .withNewMetadata().withName(id+"pay").endMetadata()
                        .withNewSpec()
                        .withNewSelector()
                        .withMatchLabels(Collections.singletonMap("app",id+"pay"))
                        .endSelector()
                        .withReplicas(1)
                        .withNewTemplate()
                        .withNewMetadata().addToLabels("app",id+"pay").endMetadata()
                        .withNewSpec()
                        .addNewContainer()
                        .withName(id+"pay")
                        .withImage("godnjs0415/finalpay2:latest")
                        .addNewPort().withContainerPort(8080).endPort()
                        .addNewEnv().withName("changefile").withNewValue(id+"pay").endEnv()
                        .addNewEnv().withName("nodeport").withNewValue(node).endEnv()
                        .endContainer()
                        .endSpec()
                        .endTemplate()
                        .endSpec()
                        .build();
                        
               //System.out.println(Yaml.dump(v1Deployment3));
              AppsV1Api appsV1Api3 = new AppsV1Api(client);
              v1Deployment3 = appsV1Api3.createNamespacedDeployment("default",v1Deployment3,null,null,null);
            
         
        // 3. service yaml 파일 만들기       
              
               // 3-1) login service yaml 파일
               V1Service loginsvc =
                    new V1ServiceBuilder()
                        .withApiVersion("v1")
                        .withKind("Service")
                        .withNewMetadata().withName(id+"login").addToLabels("app",id+"login").endMetadata()
                        .withNewSpec()
                        .withType("NodePort")
                        .addNewPort()
                        .withProtocol("TCP")
                        .withName("http")
                        .withPort(80)
                        .withTargetPort(new IntOrString(8080))
                        .endPort()
                        .addToSelector("app",id+"login")
                        .endSpec()
                        .build();
                System.out.println(Yaml.dump(loginsvc));
                loginsvc = api.createNamespacedService("default", loginsvc, null, null, null);
                int loginport= port;
           
                // 3-2) shop service yaml 파일
                V1Service shopsvc =
                        new V1ServiceBuilder()
                            .withApiVersion("v1")
                            .withKind("Service")
                            .withNewMetadata().withName(id+"shop").addToLabels("app",id+"shop").endMetadata()
                            .withNewSpec()
                            .withType("NodePort")
                            .addNewPort()
                            .withProtocol("TCP")
                            .withName("http")
                            .withPort(80)
                            .withTargetPort(new IntOrString(8080))
                            .endPort()
                            .addToSelector("app",id+"shop")
                            .endSpec()
                            .build();
                //System.out.println(Yaml.dump(shopsvc));   
                shopsvc = api.createNamespacedService("default", shopsvc, null, null, null);
                int shopport = port;
        
                // 3-3) pay service yaml 파일 
                V1Service paysvc =
                            new V1ServiceBuilder()
                                .withApiVersion("v1")
                                .withKind("Service")
                                .withNewMetadata().withName(id+"pay").addToLabels("app",id+"pay").endMetadata()
                                .withNewSpec()
                                .withType("NodePort")
                                .addNewPort()
                                .withProtocol("TCP")
                                .withName("http")
                                .withPort(80)
                                .withTargetPort(new IntOrString(8080))
                                .endPort()
                                .addToSelector("app",id+"pay")
                                .endSpec()
                                .build();
                //System.out.println(Yaml.dump(paysvc));     
                paysvc = api.createNamespacedService("default", paysvc, null, null, null);
               
                
                //4. ingress yaml 파일 만들기 
                
                //delpoy svc ingress => 애들 마다 
                // ingress 확장 ? 
                NetworkingV1beta1Api networkingV1beta1Api = new NetworkingV1beta1Api ();
        
                NetworkingV1beta1Ingress ingress = new NetworkingV1beta1IngressBuilder()
                		.withNewMetadata()
                        .withName(id+"ingress").endMetadata()
                        .withNewSpec()
                        .addNewRule()
                        .withNewHttp()
                        .addNewPath()
                        .withNewPath("/"+id+"login")
                        .withNewBackend()
                        .withNewServiceName(id+"login")
                        .withNewServicePort(80).endBackend().endPath()
                        .addNewPath()
                        .withNewPath("/"+id+"shop") //.withNewPath("/"+id+"shop"+"/*")
                        .withNewBackend()
                        .withNewServiceName(id+"shop")
                        .withNewServicePort(80).endBackend().endPath()
                        .addNewPath()
                        .withNewPath("/"+id+"pay")
                        .withNewBackend()
                        .withNewServiceName(id+"pay")
                        .withNewServicePort(80).endBackend()
                        .endPath()
                        .endHttp()
                        .endRule()
                        .endSpec()
                        .build();
                System.out.println(Yaml.dump(ingress)); 
                ApiResponse ingressCreateResult = networkingV1beta1Api.createNamespacedIngressWithHttpInfo("default", ingress, null, null, null);
      
      /*          
      //5. database pod 추가하기 
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
                      .withType("NodePort")
                      .addNewPort()
                      .withProtocol("TCP")
                      .withName("http")
                      .withNodePort(database_nodeport) //여기서 database의 nodeport를 설정하고 
                      .withPort(8889)
                      .withTargetPort(new IntOrString(3306))
                      .endPort()
                      .addToSelector("app",id+"database")
                      .endSpec()
                      .build();
              System.out.println(Yaml.dump(databasesvc));
              databasesvc = api.createNamespacedService("default", databasesvc, null, null, null); 
                
        
        	
              */  
                
                
           
    	return last;
    }
    
  
  
}



