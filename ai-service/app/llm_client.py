import os
from dotenv import load_dotenv
from langchain.chat_models import init_chat_model
from langchain_core.prompts import ChatPromptTemplate

load_dotenv(override=True)

## For LangSmith Tracking
os.environ["LANGSMITH_TRACING"] = "true"
os.environ["LANGSMITH_API_KEY"] = os.getenv("LANGSMITH_API_KEY") or os.getenv("LANGCHAIN_API_KEY")
os.environ["LANGSMITH_PROJECT"] = os.getenv("LANGSMITH_PROJECT") or os.getenv("LANGCHAIN_PROJECT")
 
## Usually optional for hosted LangSmith, but safe to set
os.environ["LANGSMITH_ENDPOINT"] = os.getenv("LANGSMITH_ENDPOINT")

llm = init_chat_model(
    "gpt-4o-mini",
    model_provider="openai",
    api_key=os.getenv("OPENAI_API_KEY"),
    temperature=0.7,
)

prompt = ChatPromptTemplate.from_messages([
    ("system", "You are an exper travel assistant. Answer the following question."),
    ("user", "{query}")
])

chain = prompt | llm

## Function that will be called by the api for the LLM call
def get_trip_response(query: str) -> str:
    res = chain.invoke({"query": query})
    return res.content
